package org.example.invest.service;

import org.apache.commons.lang3.StringUtils;
import org.example.invest.client.NseClient;
import org.example.invest.dto.EtfData;
import org.example.invest.dto.IndexData;
import org.example.invest.dto.NseAllIndicesResponse;
import org.example.invest.dto.NseEtfResponse;
import org.example.invest.util.NseCookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
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
            indexData.setYearLowToYearHighDiffPer(getYearLowToYearHighDiffPer(indexData));
            indexData.setLatestToYearLowDiffPer(getLatestToYearLowDiffPer(indexData));
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
        return process(getAllIndices(null));
    }

    public NseAllIndicesResponse getInvestableIndices() {
        NseAllIndicesResponse nseAllIndicesResponse = getAllIndices();
        if (nseAllIndicesResponse == null || CollectionUtils.isEmpty(nseAllIndicesResponse.getData())) {
            return null;
        }
        nseAllIndicesResponse.setData(nseAllIndicesResponse.getData().stream()
                .filter(indexData -> indexData != null
                        && indexData.getLatestToYearLowDiffPer() < 12D
                        && indexData.getYearLowToYearHighDiffPer() > 12D
                        && indexData.getPe() != null && indexData.getPe() <= 22D
                        && indexData.getPb() != null && indexData.getPb() <= 3.5D
                        && indexData.getDy() != null && indexData.getDy() >= 1.2D)
                .collect(Collectors.toList()));
        //PE(Price/Earning ratio) for, Undervalue: <16 for nifty & <20 for sector. fair: 16-22 for nifty
        //PB(Price/Booking ratio) for, deep value: <2.5. fair: <=3.5
        //DY(Dividend yield) for, cheap market: >2%. fair: >=1.2%.
        return nseAllIndicesResponse;
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
    public NseEtfResponse getEtfData(String cookie, String index) {
        try {
            return process(nseClient.getEtfData(cookie), index);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching NSE ETF data: " + e.getMessage(), e);
        }
    }

    private NseEtfResponse process(NseEtfResponse etfData, String index) {
        filter(etfData, index);
        deriveMetrics(etfData);
        sort(etfData);
        return etfData;
    }

    private void filter(NseEtfResponse etfData, String index) {
        String subStr = removeHypnUnderScorSpcAndGetLowerCase(index);
        if (StringUtils.isEmpty(subStr) || etfData == null || CollectionUtils.isEmpty(etfData.getData())) {
            return;
        }
        etfData.setData(etfData.getData().parallelStream()
                .filter(etf -> etf != null
                        && isEtfOfProvidedIndex(subStr, etf))
                .collect(Collectors.toList()));
    }

    private boolean isEtfOfProvidedIndex(String subStr, EtfData etf) {
        String assetStr = removeHypnUnderScorSpcAndGetLowerCase(etf.getAssets());
        String companyNameStr = removeHypnUnderScorSpcAndGetLowerCase(etf.getCompanyName());
        return (assetStr.contains(subStr)
                || companyNameStr.contains(subStr));
    }

    private void deriveMetrics(NseEtfResponse etfData) {
        if (etfData == null || CollectionUtils.isEmpty(etfData.getData())) {
            return;
        }
        for (EtfData etf : etfData.getData()) {
            etf.setNavToMarketLtPDelta(getDelta(etf.getLtP(), etf.getNav()));
            etf.setNavToMarketLtPDeltaPercent(getDeltaPercent(etf.getNav(), etf.getLtP()));
        }
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
            if (ZERO_D.equals(diff) && etf1.getQty() != null && etf2.getQty() != null) {
                diff = Double.compare(etf2.getQty(), etf1.getQty());
            }
            if (ZERO_D.equals(diff) && etf1.getPer() != null && etf2.getPer() != null) {
                diff = Double.compare(etf1.getPer(), etf2.getPer());
            }
            return diff;
        });
    }

    private String removeHypnUnderScorSpcAndGetLowerCase(String str) {
        if (StringUtils.isEmpty(str)) {
            return StringUtils.EMPTY;
        }
        String subStr = str.replaceAll("-", "");
        subStr = str.replaceAll("_", "");
        subStr = str.replaceAll(" ", "");
        return subStr.toLowerCase();
    }

    /**
     * Get ETF data from NSE using default cookie
     *
     * @return NSE ETF response with detailed ETF data
     */
    public NseEtfResponse getEtfData() {
        try {
            String defaultCookie = NseCookieUtil.getDefaultCookie();
            return getEtfData(defaultCookie, null);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching NSE ETF data: " + e.getMessage(), e);
        }
    }
}
