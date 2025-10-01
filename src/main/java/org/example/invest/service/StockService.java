package org.example.invest.service;

import org.apache.commons.lang3.StringUtils;
import org.example.invest.client.NseClient;
import org.example.invest.dto.EtfData;
import org.example.invest.dto.IndexData;
import org.example.invest.dto.NseAllIndicesResponse;
import org.example.invest.dto.NseEtfResponse;
import org.example.invest.dto.ProcessDto;
import org.example.invest.util.NseCookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class demonstrating how to use Feign client in Java 11
 * This service uses the NseClient Feign interface to make HTTP calls
 */
@Service
public class StockService {

    public static final Double ZERO_D = 0D;
    public static final Double HUNDREAD_D = 100D;

    @Autowired
    private NseClient nseClient;

    /**
     * Get all NSE indices data
     *
     * @param cookie Cookie header for NSE API authentication (optional)
     * @return NSE all indices response
     */
    public NseAllIndicesResponse getAllIndices(String cookie) {
        try {
            //String validCookie = NseCookieUtil.getCookieOrDefault(cookie);
            return process(nseClient.getAllIndices(cookie));
        } catch (Exception e) {
            throw new RuntimeException("Error fetching NSE indices data: " + e.getMessage(), e);
        }
    }

    private NseAllIndicesResponse process(NseAllIndicesResponse allIndices) {
        deriveMetrics(allIndices);
        sort(allIndices);
        return allIndices;
    }

    private void deriveMetrics(NseAllIndicesResponse allIndices) {
        for (IndexData indexData : allIndices.getData()) {
            indexData.setProcessedIndex(removeHypnUnderScorSpcSecIndAndGetLowerCase(indexData.getIndex()));
            indexData.setProcessedIndexSymbol(removeHypnUnderScorSpcSecIndAndGetLowerCase(indexData.getIndex()));
            indexData.setYearLowToYearHighDiffPer(getYearLowToYearHighDiffPer(indexData));
            indexData.setLatestToYearLowDiffPer(getLatestToYearLowDiffPer(indexData));
            indexData.setYearHighToLatestDiffPer(getDeltaPercent(indexData.getYearHigh(), indexData.getLast()));
            indexData.setLatestToLastWeekDiffPer(getLatestToLastWeekDiffPer(indexData));
        }
    }

    private Double getLatestToLastWeekDiffPer(IndexData indexData) {
        return getDeltaPercent(indexData.getLast(), indexData.getOneWeekAgoVal());
    }

    private Double getYearLowToYearHighDiffPer(IndexData indexData) {
        return getDeltaPercent(indexData.getYearHigh(), indexData.getYearLow());
    }

    private Double getLatestToYearLowDiffPer(IndexData indexData) {
        return getDeltaPercent(indexData.getLast(), indexData.getYearLow());
    }

    private void sort(NseAllIndicesResponse allIndices) {
        Collections.sort(allIndices.getData(), (ind1, ind2) -> {
            int diff = Double.compare(ind1.getLatestToYearLowDiffPer(), ind2.getLatestToYearLowDiffPer());
            if (ZERO_D.equals(diff)) {
                diff = Double.compare(ind2.getYearLowToYearHighDiffPer(), ind1.getYearLowToYearHighDiffPer());
            }
            if (ZERO_D.equals(diff) && ind1.getPe() != null && ind2.getPe() != null) {
                diff = Double.compare(ind1.getPe(), ind2.getPe());
            }
            if (ZERO_D.equals(diff) && ind1.getDy() != null && ind2.getDy() != null) {
                diff = Double.compare(ind2.getDy(), ind1.getDy());
            }
            if (ZERO_D.equals(diff) && ind1.getPb() != null && ind2.getPb() != null) {
                diff = Double.compare(ind1.getPb(), ind2.getPb());
            }
            if (ZERO_D.equals(diff) && ind1.getAdvances() != null && ind2.getAdvances() != null) {
                diff = Long.compare(ind2.getAdvances(), ind1.getAdvances());
            }
            if (ZERO_D.equals(diff) && ind1.getDeclines() != null && ind2.getDeclines() != null) {
                diff = Long.compare(ind1.getDeclines(), ind2.getDeclines());
            }
            if (ZERO_D.equals(diff) && ind1.getUnchanged() != null && ind2.getUnchanged() != null) {
                diff = Long.compare(ind2.getUnchanged(), ind1.getUnchanged());
            }
            return diff;
        });
    }

    /**
     * Get all NSE indices data using default cookie
     *
     * @return NSE all indices response
     */
    public NseAllIndicesResponse getAllIndices() {
        try {
            String defaultCookie = NseCookieUtil.getDefaultCookie();
            return process(nseClient.getAllIndices(defaultCookie));
        } catch (Exception e) {
            throw new RuntimeException("Error fetching NSE indices data: " + e.getMessage(), e);
        }
    }

    public NseAllIndicesResponse getInvestableIndices(String cookie) {
        NseAllIndicesResponse nseAllIndicesResponse = getAllIndices(cookie);
        if (nseAllIndicesResponse == null || CollectionUtils.isEmpty(nseAllIndicesResponse.getData())) {
            return null;
        }
        nseAllIndicesResponse.setData(nseAllIndicesResponse.getData().stream()
                .filter(indexData -> isInvestableIndex(indexData))
                .collect(Collectors.toList()));
        //PE(Price/Earning ratio) for, Undervalue: <16 for nifty & <20 for sector. fair: 16-22 for nifty
        //PB(Price/Booking ratio) for, deep value: <2.5. fair: <=3.5
        //DY(Dividend yield) for, cheap market: >2%. fair: >=1.2%.
        return nseAllIndicesResponse;
    }

    private boolean isInvestableIndex(IndexData indexData) {
        return (indexData != null
                && StringUtils.isNotEmpty(indexData.getDate365dAgo())
                && indexData.getYearLow() != null && indexData.getYearLow() > ZERO_D
                && indexData.getYearHigh() != null && indexData.getYearHigh() > ZERO_D
                && indexData.getOneYearAgoVal() != null && indexData.getOneYearAgoVal() > ZERO_D
                && indexData.getYearHigh() > indexData.getOneYearAgoVal()
                && indexData.getLatestToYearLowDiffPer() != null && indexData.getLatestToYearLowDiffPer() < 18D//Ideally indexData.getLatestToYearLowDiffPer() < 12D
                && indexData.getYearLowToYearHighDiffPer() != null && indexData.getYearLowToYearHighDiffPer() > 10D //Yearly 10% change
                && indexData.getYearHighToLatestDiffPer() != null && indexData.getYearHighToLatestDiffPer() > 4D//Makes sure we dont buy at year high
                && indexData.getPe() != null && indexData.getPe() <= 22D
                && indexData.getPb() != null && indexData.getPb() <= 3.5D
                && indexData.getDy() != null && indexData.getDy() >= 1.2D);
    }

    /**
     * Get stock quote for a given symbol
     *
     * @param symbol Stock symbol
     * @return Stock quote information
     */
    public String getStockQuote(String symbol) {
        try {
            return nseClient.getStockQuote(symbol);
        } catch (Exception e) {
            return "Error fetching stock quote for " + symbol + ": " + e.getMessage();
        }
    }

    /**
     * Get current market status
     *
     * @return Market status information
     */
    public String getMarketStatus() {
        try {
            return nseClient.getMarketStatus();
        } catch (Exception e) {
            return "Error fetching market status: " + e.getMessage();
        }
    }

    /**
     * Get company information by symbol
     *
     * @param symbol Stock symbol
     * @return Company information
     */
    public String getCompanyInfo(String symbol) {
        try {
            return nseClient.getCompanyInfo(symbol);
        } catch (Exception e) {
            return "Error fetching company info for " + symbol + ": " + e.getMessage();
        }
    }

    /**
     * Get ETF data from NSE
     *
     * @param cookie Cookie header for NSE API authentication (optional)
     * @return NSE ETF response with detailed ETF data
     */
    public NseEtfResponse getEtfData(String cookie, String index, String indexSymbol) {
        try {
            return process(nseClient.getEtfData(cookie), index, indexSymbol);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching NSE ETF data: " + e.getMessage(), e);
        }
    }

    private NseEtfResponse process(NseEtfResponse etfData, String index, String indexSymbol) {
        deriveMetrics(etfData);
        filter(etfData, index, indexSymbol);
        sort(etfData);
        return etfData;
    }

    private void filter(NseEtfResponse etfData, String index, String indexSymbol) {
        String indexSubStr = removeHypnUnderScorSpcSecIndAndGetLowerCase(index);
        String indexSymbolSubStr = removeHypnUnderScorSpcSecIndAndGetLowerCase(indexSymbol);
        if ((StringUtils.isEmpty(indexSubStr) && StringUtils.isEmpty(indexSymbolSubStr))
                || etfData == null || CollectionUtils.isEmpty(etfData.getData())) {
            return;
        }
        etfData.setData(etfData.getData().parallelStream()
                .filter(etf -> etf != null
                        && isEtfOfProvidedIndex(indexSubStr, indexSymbolSubStr, etf))
                .collect(Collectors.toList()));
    }

    private boolean isEtfOfProvidedIndex(String indexSubStr, String indexSymbolSubStr, EtfData etf) {
        if (StringUtils.isEmpty(indexSubStr) && StringUtils.isEmpty(indexSymbolSubStr)) {
            return true; // No index specified, so no match
        }
        if (StringUtils.isEmpty(etf.getProcessedAssets()) && StringUtils.isEmpty(etf.getProcessedCompanyName())) {
            return false;
        }
        return (
                (StringUtils.isNotEmpty(etf.getProcessedAssets()) && (etf.getProcessedAssets().contains(indexSubStr)
                        || etf.getProcessedAssets().contains(indexSymbolSubStr)))
                        || (StringUtils.isNotEmpty(etf.getProcessedCompanyName()) && (etf.getProcessedCompanyName().contains(indexSubStr)
                        || etf.getProcessedCompanyName().contains(indexSymbolSubStr)))
        );
    }

    private void deriveMetrics(NseEtfResponse etfData) {
        if (etfData == null || CollectionUtils.isEmpty(etfData.getData())) {
            return;
        }
        for (EtfData etf : etfData.getData()) {
            etf.setProcessedAssets(removeHypnUnderScorSpcSecIndAndGetLowerCase(etf.getAssets()));
            etf.setProcessedcompanyName(removeHypnUnderScorSpcSecIndAndGetLowerCase(etf.getCompanyName()));
            etf.setNavToMarketLtPDelta(getDelta(etf.getLtP(), etf.getNav()));
            etf.setNavToMarketLtPDeltaPercent(getDeltaPercent(etf.getNav(), etf.getLtP()));
            etf.setYearLowToYearHighDiffPer(getYearLowToYearHighDiffPer(etf));
            etf.setLatestToYearLowDiffPer(getLatestToYearLowDiffPer(etf));//nearWKL*-1
        }
    }

    private Double getYearLowToYearHighDiffPer(EtfData etf) {
        return getDeltaPercent(etf.getWkhi(), etf.getWklo());
    }

    private Double getLatestToYearLowDiffPer(EtfData etf) {
        return getDeltaPercent(etf.getLtP(), etf.getWklo());//nearWKL*-1
    }

    private Double getDelta(Double val1, Double val2) {
        if (val1 == null || val2 == null) {
            return ZERO_D;
        }
        return val1 - val2;
    }

    private Double getDeltaPercent(Double numerator, Double denomerator) {
        if (numerator == null || denomerator == null || ZERO_D.equals(denomerator)) {
            return Double.MAX_VALUE;
        }
        return ((numerator / denomerator * HUNDREAD_D) - HUNDREAD_D);
    }

    private void sort(NseEtfResponse etfData) {
        if (etfData == null || CollectionUtils.isEmpty(etfData.getData())) {
            return;
        }
        Collections.sort(etfData.getData(), (etf1, etf2) -> {
            int diff = Double.compare(etf2.getNavToMarketLtPDeltaPercent(), etf1.getNavToMarketLtPDeltaPercent());
            if (ZERO_D.equals(diff)) {
                diff = Double.compare(etf1.getLatestToYearLowDiffPer(), etf2.getLatestToYearLowDiffPer());
            }
            if (ZERO_D.equals(diff)) {
                diff = Double.compare(etf1.getYearLowToYearHighDiffPer(), etf2.getYearLowToYearHighDiffPer());
            }
            if (ZERO_D.equals(diff) && etf1.getQty() != null && etf2.getQty() != null) {
                diff = Double.compare(etf2.getQty(), etf1.getQty());
            }
            if (ZERO_D.equals(diff) && etf1.getPer() != null && etf2.getPer() != null) {
                diff = Double.compare(etf1.getPer(), etf2.getPer());
            }
            return diff;
        });
    }

    private String removeHypnUnderScorSpcSecIndAndGetLowerCase(String str) {
        if (StringUtils.isEmpty(str)) {
            return StringUtils.EMPTY;
        }
        String subStr = str.toLowerCase();
        subStr = subStr.replaceAll("-", "");
        subStr = subStr.replaceAll("_", "");
        subStr = subStr.replaceAll(" ", "");
        subStr = subStr.replaceAll("sector", "");
        subStr = subStr.replaceAll("sec", "");
        subStr = subStr.replaceAll("index", "");
        return subStr;
    }

    /**
     * Get ETF data from NSE using default cookie
     *
     * @return NSE ETF response with detailed ETF data
     */
    public NseEtfResponse getEtfData() {
        try {
            String defaultCookie = NseCookieUtil.getDefaultCookie();
            return getEtfData(defaultCookie, null, null);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching NSE ETF data: " + e.getMessage(), e);
        }
    }

    public NseEtfResponse getInvestableEtfData(String cookie, String index, String indexSymbol) {
        ProcessDto processDto = getProcessDto(cookie);
        return processDto.getInvestableEtf();
        /*
        NseEtfResponse nseEtfResponse = getEtfData(cookie, index, indexSymbol);
        if (nseEtfResponse == null || CollectionUtils.isEmpty(nseEtfResponse.getData())) {
            return null;
        }
        NseAllIndicesResponse investableIndices = getInvestableIndices(cookie);
        nseEtfResponse.setData(nseEtfResponse.getData().stream()
                .filter(etfData -> etfData != null
                        && StringUtils.isNotEmpty(etfData.getDate365dAgo())
                        && (isEtfOfIndex(investableIndices, etfData) || isPerformingEtf(etfData)))
                .collect(Collectors.toList()));
        sort(nseEtfResponse);
        return nseEtfResponse;
        */
    }

    private ProcessDto getProcessDto(String cookie) {
        ProcessDto processDto = new ProcessDto();

        processDto.setAllIndices(getAllIndices(cookie));
        setInvestableIndices(processDto);

        setEtfWithIndexData(cookie, processDto);
        setInvestableEtf(processDto);

        return processDto;
    }

    private void setInvestableIndices(ProcessDto processDto) {
        if (processDto == null || processDto.getAllIndices() == null) {
            return;
        }
        processDto.setInvestableIndices(new NseAllIndicesResponse(getInvestableIndicesData(processDto)
                , processDto.getAllIndices().getTimestamp()
                , processDto.getAllIndices().getAdvances()
                , processDto.getAllIndices().getDeclines()
                , processDto.getAllIndices().getUnchanged()
                , processDto.getAllIndices().getDates()
        ));
    }

    private List<IndexData> getInvestableIndicesData(ProcessDto processDto) {
        if (processDto == null || processDto.getAllIndices() == null
                || CollectionUtils.isEmpty(processDto.getAllIndices().getData())) {
            return null;
        }
        return processDto.getAllIndices().getData().stream()
                .filter(indexData -> isInvestableIndex(indexData))
                .collect(Collectors.toList());
    }

    private void setEtfWithIndexData(String cookie, ProcessDto processDto) {
        NseEtfResponse nseEtfResponse = nseClient.getEtfData(cookie);
        deriveMetricsWithInd(nseEtfResponse, processDto);
        sort(nseEtfResponse);
        processDto.setAllEtf(nseEtfResponse);
    }

    private void deriveMetricsWithInd(NseEtfResponse nseEtfResponse, ProcessDto processDto) {
        if (nseEtfResponse == null || CollectionUtils.isEmpty(nseEtfResponse.getData())) {
            return;
        }
        for (EtfData etf : nseEtfResponse.getData()) {
            etf.setProcessedAssets(removeHypnUnderScorSpcSecIndAndGetLowerCase(etf.getAssets()));
            etf.setProcessedcompanyName(removeHypnUnderScorSpcSecIndAndGetLowerCase(etf.getCompanyName()));
            etf.setNavToMarketLtPDelta(getDelta(etf.getLtP(), etf.getNav()));
            etf.setNavToMarketLtPDeltaPercent(getDeltaPercent(etf.getNav(), etf.getLtP()));
            etf.setYearLowToYearHighDiffPer(getYearLowToYearHighDiffPer(etf));
            etf.setLatestToYearLowDiffPer(getLatestToYearLowDiffPer(etf));//nearWKL*-1
            setIndDataInEtf(etf, processDto);
        }
    }

    private void setIndDataInEtf(EtfData etf, ProcessDto processDto) {
        if (etf == null || processDto == null || processDto.getAllIndices() == null
                || CollectionUtils.isEmpty(processDto.getAllIndices().getData())) {
            return;
        }
        for (IndexData indexData : processDto.getAllIndices().getData()) {
            if (isEtfOfProvidedIndex(indexData.getProcessedIndex(), indexData.getProcessedIndexSymbol(), etf)) {
                etf.setIndexData(indexData);
                break;
            }
        }
    }

    private void setInvestableEtf(ProcessDto processDto) {
        if (processDto == null || processDto.getAllEtf() == null || CollectionUtils.isEmpty(processDto.getAllEtf().getData())) {
            return;
        }

        processDto.setInvestableEtf(new NseEtfResponse(processDto.getAllEtf().getTimestamp()
                        , getInvestableEtfData(processDto)//, getInvestableEtfData(processDto.getAllEtf().getData())
                        , processDto.getAllEtf().getAdvances()
                        , processDto.getAllEtf().getDeclines()
                        , processDto.getAllEtf().getUnchanged()
                        , processDto.getAllEtf().getNavDate()
                        , processDto.getAllEtf().getTotalTradedValue()
                        , processDto.getAllEtf().getTotalTradedVolume()
                        , processDto.getAllEtf().getMarketStatus()
                )

        );
    }

    private List<EtfData> getInvestableEtfData(ProcessDto processDto) {
        if (processDto == null || processDto.getAllEtf() == null
                || CollectionUtils.isEmpty(processDto.getAllEtf().getData())) {
            return null;
        }
        return processDto.getAllEtf().getData().stream()
                .filter(etfData -> etfData != null
                        && StringUtils.isNotEmpty(etfData.getDate365dAgo())
                        && etfData.getQty() != null && etfData.getQty() > 10000L //ideally, etfData.getQty() > 20,000L
                        && (isInvestableIndex(processDto, etfData) || isPerformingEtf(etfData)))
                .collect(Collectors.toList());
    }

    private boolean isInvestableIndex(ProcessDto processDto, EtfData etfData) {
        if (etfData == null || etfData.getIndexData() == null
                || processDto == null || processDto.getInvestableIndices() == null
                || CollectionUtils.isEmpty(processDto.getInvestableIndices().getData())) {
            return false;
        }
        return processDto.getInvestableIndices().getData().contains(etfData.getIndexData());
    }

    private List<EtfData> getInvestableEtfData(List<EtfData> data) {
        return data.stream()
                .filter(etfData -> etfData != null
                        && StringUtils.isNotEmpty(etfData.getDate365dAgo())
                        && (isInvestableIndex(etfData.getIndexData()) || isPerformingEtf(etfData)))
                .collect(Collectors.toList());
    }

    private boolean isEtfOfIndex(NseAllIndicesResponse indices, EtfData etfData) {
        if (indices == null || CollectionUtils.isEmpty(indices.getData())) {
            return false;
        }
        for (IndexData indexData : indices.getData()) {
            if (isEtfOfProvidedIndex(indexData.getProcessedIndex(), indexData.getProcessedIndexSymbol(), etfData)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPerformingEtf(EtfData etfData) {
        return (etfData != null
                && etfData.getLatestToYearLowDiffPer() != null && etfData.getLatestToYearLowDiffPer() < 18D //ideally, etfData.getLatestToYearLowDiffPer() < 12D
                && etfData.getYearLowToYearHighDiffPer() != null && etfData.getYearLowToYearHighDiffPer() > 10D //Atleast 10% changes in year
                && etfData.getNearWKH() != null && etfData.getNearWKH() > 4D//Makes sure we dont buy at year high
                && etfData.getNavToMarketLtPDeltaPercent() != null && etfData.getNavToMarketLtPDeltaPercent() > -0.2D);//not buying too expensive from NAV
    }
}
