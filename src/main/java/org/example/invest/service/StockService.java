package org.example.invest.service;

import org.apache.commons.lang3.StringUtils;
import org.example.invest.client.NseClient;
import org.example.invest.dto.nse.etf.EtfData;
import org.example.invest.dto.nse.index.NseIndexData;
import org.example.invest.dto.nse.index.NseAllIndicesResponse;
import org.example.invest.dto.nse.etf.NseEtfResponse;
import org.example.invest.dto.OldProcessDto;
import org.example.invest.util.GeneralUtil;
import org.example.invest.util.NseCookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.ShortlistCriteriaConfig.LEAST_ETF_VOLUME;
import static org.example.ShortlistCriteriaConfig.LEAST_NavToMarketLtPDeltaPercent_D;
import static org.example.ShortlistCriteriaConfig.LEAST_YearHighToLatestDiffPer_D;
import static org.example.ShortlistCriteriaConfig.LEAST_YearHighToYearLowDiffPer_D;
import static org.example.ShortlistCriteriaConfig.MAX_LTP_TO_YRLOW_DIFF_PER_D;

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

    @Autowired
    private GeneralUtil generalUtil;

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
        for (NseIndexData nseIndexData : allIndices.getData()) {
            nseIndexData.setProcessedIndex(removeHypnUnderScorSpcSecIndAndGetLowerCase(nseIndexData.getIndex()));
            nseIndexData.setProcessedIndexSymbol(removeHypnUnderScorSpcSecIndAndGetLowerCase(nseIndexData.getIndex()));
            nseIndexData.setYearHighToYearLowDiffPer(getYearHighToYearLowDiffPer(nseIndexData));
            nseIndexData.setLatestToYearLowDiffPer(getLatestToYearLowDiffPer(nseIndexData));
            nseIndexData.setYearHighToLatestDiffPer(getDeltaPercent(nseIndexData.getYearHigh(), nseIndexData.getLast()));
            nseIndexData.setLatestToLastWeekDiffPer(getLatestToLastWeekDiffPer(nseIndexData));
        }
    }

    private Double getLatestToLastWeekDiffPer(NseIndexData nseIndexData) {
        return getDeltaPercent(nseIndexData.getLast(), nseIndexData.getOneWeekAgoVal());
    }

    private Double getYearHighToYearLowDiffPer(NseIndexData nseIndexData) {
        return getDeltaPercent(nseIndexData.getYearHigh(), nseIndexData.getYearLow());
    }

    private Double getLatestToYearLowDiffPer(NseIndexData nseIndexData) {
        return getDeltaPercent(nseIndexData.getLast(), nseIndexData.getYearLow());
    }

    private void sort(NseAllIndicesResponse allIndices) {
        Collections.sort(allIndices.getData(), (ind1, ind2) -> {
            int diff = generalUtil.compareWithNullInLast(ind1.getLatestToYearLowDiffPer(), ind2.getLatestToYearLowDiffPer());
            if (ZERO_D.equals(diff)) {
                diff = generalUtil.compareWithNullInLast(ind2.getYearHighToYearLowDiffPer(), ind1.getYearHighToYearLowDiffPer());
            }
            if (ZERO_D.equals(diff) && ind1.getPe() != null && ind2.getPe() != null) {
                diff = generalUtil.compareWithNullInLast(ind1.getPe(), ind2.getPe());
            }
            if (ZERO_D.equals(diff) && ind1.getDy() != null && ind2.getDy() != null) {
                diff = generalUtil.compareWithNullInLast(ind2.getDy(), ind1.getDy());
            }
            if (ZERO_D.equals(diff) && ind1.getPb() != null && ind2.getPb() != null) {
                diff = generalUtil.compareWithNullInLast(ind1.getPb(), ind2.getPb());
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
                .filter(nseIndexData -> isInvestableIndex(nseIndexData))
                .collect(Collectors.toList()));
        //PE(Price/Earning ratio) for, Undervalue: <16 for nifty & <20 for sector. fair: 16-22 for nifty
        //PB(Price/Booking ratio) for, deep value: <2.5. fair: <=3.5
        //DY(Dividend yield) for, cheap market: >2%. fair: >=1.2%.
        return nseAllIndicesResponse;
    }

    private boolean isInvestableIndex(NseIndexData nseIndexData) {
        return (nseIndexData != null
                && StringUtils.isNotEmpty(nseIndexData.getDate365dAgo())
                && nseIndexData.getYearLow() != null && nseIndexData.getYearLow() > ZERO_D
                && nseIndexData.getYearHigh() != null && nseIndexData.getYearHigh() > ZERO_D
                && nseIndexData.getOneYearAgoVal() != null && nseIndexData.getOneYearAgoVal() > ZERO_D
                && nseIndexData.getYearHigh() > nseIndexData.getOneYearAgoVal()
                && nseIndexData.getLatestToYearLowDiffPer() != null && nseIndexData.getLatestToYearLowDiffPer() < 12D//Ideally nseIndexData.getLatestToYearLowDiffPer() < 12D
                && nseIndexData.getYearHighToYearLowDiffPer() != null && nseIndexData.getYearHighToYearLowDiffPer() > 10D //Yearly 10% change
                && nseIndexData.getYearHighToLatestDiffPer() != null && nseIndexData.getYearHighToLatestDiffPer() > 4D//Makes sure we dont buy at year high
                && nseIndexData.getPe() != null && nseIndexData.getPe() <= 22D
                && nseIndexData.getPb() != null && nseIndexData.getPb() <= 3.5D
                && nseIndexData.getDy() != null && nseIndexData.getDy() >= 1.2D);
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
            etf.setProcessedCompanyName(removeHypnUnderScorSpcSecIndAndGetLowerCase(etf.getCompanyName()));
            etf.setNavToMarketLtPDelta(getDelta(etf.getLtP(), etf.getNav()));
            etf.setNavToMarketLtPDeltaPercent(getDeltaPercent(etf.getNav(), etf.getLtP()));
            etf.setYearHighToYearLowDiffPer(getYearHighToYearLowDiffPer(etf));
            etf.setLatestToYearLowDiffPer(getLatestToYearLowDiffPer(etf));//nearWKL*-1
        }
    }

    private Double getYearHighToYearLowDiffPer(EtfData etf) {
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
            int diff = generalUtil.compareWithNullInLast(etf2.getNavToMarketLtPDeltaPercent(), etf1.getNavToMarketLtPDeltaPercent());
            if (ZERO_D.equals(diff)) {
                diff = generalUtil.compareWithNullInLast(etf1.getLatestToYearLowDiffPer(), etf2.getLatestToYearLowDiffPer());
            }
            if (ZERO_D.equals(diff)) {
                diff = generalUtil.compareWithNullInLast(etf1.getYearHighToYearLowDiffPer(), etf2.getYearHighToYearLowDiffPer());
            }
            if (ZERO_D.equals(diff) && etf1.getQty() != null && etf2.getQty() != null) {
                diff = generalUtil.compareWithNullInLast(etf2.getQty(), etf1.getQty());
            }
            if (ZERO_D.equals(diff) && etf1.getPer() != null && etf2.getPer() != null) {
                diff = generalUtil.compareWithNullInLast(etf1.getPer(), etf2.getPer());
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
        OldProcessDto oldProcessDto = getProcessDto(cookie);
        return oldProcessDto.getInvestableEtf();
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

    private OldProcessDto getProcessDto(String cookie) {
        OldProcessDto oldProcessDto = new OldProcessDto();

        oldProcessDto.setAllIndices(getAllIndices(cookie));
        setInvestableIndices(oldProcessDto);

        setEtfWithIndexData(cookie, oldProcessDto);
        setInvestableEtf(oldProcessDto);

        return oldProcessDto;
    }

    private void setInvestableIndices(OldProcessDto oldProcessDto) {
        if (oldProcessDto == null || oldProcessDto.getAllIndices() == null) {
            return;
        }
        oldProcessDto.setInvestableIndices(new NseAllIndicesResponse(getInvestableIndicesData(oldProcessDto)
                , oldProcessDto.getAllIndices().getTimestamp()
                , oldProcessDto.getAllIndices().getAdvances()
                , oldProcessDto.getAllIndices().getDeclines()
                , oldProcessDto.getAllIndices().getUnchanged()
                , oldProcessDto.getAllIndices().getDates()
        ));
    }

    private List<NseIndexData> getInvestableIndicesData(OldProcessDto oldProcessDto) {
        if (oldProcessDto == null || oldProcessDto.getAllIndices() == null
                || CollectionUtils.isEmpty(oldProcessDto.getAllIndices().getData())) {
            return null;
        }
        return oldProcessDto.getAllIndices().getData().stream()
                .filter(nseIndexData -> isInvestableIndex(nseIndexData))
                .collect(Collectors.toList());
    }

    private void setEtfWithIndexData(String cookie, OldProcessDto oldProcessDto) {
        NseEtfResponse nseEtfResponse = nseClient.getEtfData(cookie);
        deriveMetricsWithInd(nseEtfResponse, oldProcessDto);
        sort(nseEtfResponse);
        oldProcessDto.setAllEtf(nseEtfResponse);
    }

    private void deriveMetricsWithInd(NseEtfResponse nseEtfResponse, OldProcessDto oldProcessDto) {
        if (nseEtfResponse == null || CollectionUtils.isEmpty(nseEtfResponse.getData())) {
            return;
        }
        for (EtfData etf : nseEtfResponse.getData()) {
            etf.setProcessedAssets(removeHypnUnderScorSpcSecIndAndGetLowerCase(etf.getAssets()));
            etf.setProcessedCompanyName(removeHypnUnderScorSpcSecIndAndGetLowerCase(etf.getCompanyName()));
            etf.setNavToMarketLtPDelta(getDelta(etf.getLtP(), etf.getNav()));
            etf.setNavToMarketLtPDeltaPercent(getDeltaPercent(etf.getNav(), etf.getLtP()));
            etf.setYearHighToYearLowDiffPer(getYearHighToYearLowDiffPer(etf));
            etf.setLatestToYearLowDiffPer(getLatestToYearLowDiffPer(etf));//nearWKL*-1
            setIndDataInEtf(etf, oldProcessDto);
        }
    }

    private void setIndDataInEtf(EtfData etf, OldProcessDto oldProcessDto) {
        if (etf == null || oldProcessDto == null || oldProcessDto.getAllIndices() == null
                || CollectionUtils.isEmpty(oldProcessDto.getAllIndices().getData())) {
            return;
        }
        for (NseIndexData nseIndexData : oldProcessDto.getAllIndices().getData()) {
            if (isEtfOfProvidedIndex(nseIndexData.getProcessedIndex(), nseIndexData.getProcessedIndexSymbol(), etf)) {
                etf.setNseIndexData(nseIndexData);
                break;
            }
        }
    }

    private void setInvestableEtf(OldProcessDto oldProcessDto) {
        if (oldProcessDto == null || oldProcessDto.getAllEtf() == null || CollectionUtils.isEmpty(oldProcessDto.getAllEtf().getData())) {
            return;
        }

        oldProcessDto.setInvestableEtf(new NseEtfResponse(oldProcessDto.getAllEtf().getTimestamp()
                        , getInvestableEtfData(oldProcessDto)//, getInvestableEtfData(oldProcessDto.getAllEtf().getData())
                        , oldProcessDto.getAllEtf().getAdvances()
                        , oldProcessDto.getAllEtf().getDeclines()
                        , oldProcessDto.getAllEtf().getUnchanged()
                        , oldProcessDto.getAllEtf().getNavDate()
                        , oldProcessDto.getAllEtf().getTotalTradedValue()
                        , oldProcessDto.getAllEtf().getTotalTradedVolume()
                        , oldProcessDto.getAllEtf().getMarketStatus()
                )

        );
    }

    private List<EtfData> getInvestableEtfData(OldProcessDto oldProcessDto) {
        if (oldProcessDto == null || oldProcessDto.getAllEtf() == null
                || CollectionUtils.isEmpty(oldProcessDto.getAllEtf().getData())) {
            return null;
        }
        return oldProcessDto.getAllEtf().getData().stream()
                .filter(etfData -> etfData != null
                        && StringUtils.isNotEmpty(etfData.getDate365dAgo())
                        && etfData.getQty() != null && etfData.getQty() > LEAST_ETF_VOLUME
                        && (isInvestableIndex(oldProcessDto, etfData) || isPerformingEtf(etfData)))
                .collect(Collectors.toList());
    }

    private boolean isInvestableIndex(OldProcessDto oldProcessDto, EtfData etfData) {
        if (etfData == null || etfData.getNseIndexData() == null
                || oldProcessDto == null || oldProcessDto.getInvestableIndices() == null
                || CollectionUtils.isEmpty(oldProcessDto.getInvestableIndices().getData())) {
            return false;
        }
        return oldProcessDto.getInvestableIndices().getData().contains(etfData.getNseIndexData());
    }

    private List<EtfData> getInvestableEtfData(List<EtfData> data) {
        return data.stream()
                .filter(etfData -> etfData != null
                        && StringUtils.isNotEmpty(etfData.getDate365dAgo())
                        && (isInvestableIndex(etfData.getNseIndexData()) || isPerformingEtf(etfData)))
                .collect(Collectors.toList());
    }

    private boolean isEtfOfIndex(NseAllIndicesResponse indices, EtfData etfData) {
        if (indices == null || CollectionUtils.isEmpty(indices.getData())) {
            return false;
        }
        for (NseIndexData nseIndexData : indices.getData()) {
            if (isEtfOfProvidedIndex(nseIndexData.getProcessedIndex(), nseIndexData.getProcessedIndexSymbol(), etfData)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPerformingEtf(EtfData etfData) {
        return (etfData != null
                && etfData.getLatestToYearLowDiffPer() != null && etfData.getLatestToYearLowDiffPer() < MAX_LTP_TO_YRLOW_DIFF_PER_D
                && etfData.getYearHighToYearLowDiffPer() != null && etfData.getYearHighToYearLowDiffPer() > LEAST_YearHighToYearLowDiffPer_D
                && etfData.getNearWKH() != null && etfData.getNearWKH() > LEAST_YearHighToLatestDiffPer_D//Makes sure we dont buy at year high
                && etfData.getNavToMarketLtPDeltaPercent() != null && etfData.getNavToMarketLtPDeltaPercent() > LEAST_NavToMarketLtPDeltaPercent_D);//not buying too expensive from NAV
    }
}
