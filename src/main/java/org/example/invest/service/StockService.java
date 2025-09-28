package org.example.invest.service;

import org.example.invest.client.NseClient;
import org.example.invest.dto.IndexData;
import org.example.invest.dto.NseAllIndicesResponse;
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

    public static final Double ZERO_D=0D;

    @Autowired
    private NseClient nseClient;
    
    /**
     * Get all NSE indices data
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
        Double latestPrice = indexData.getLast();
        Double lastWeekPrice = indexData.getOneWeekAgoVal();
        if (latestPrice == null || lastWeekPrice == null || lastWeekPrice == 0.0) {
            return Double.MAX_VALUE;
        }
        return ((latestPrice / lastWeekPrice * 100) - 100);
    }

    private Double getLatestToLastMonthDiffPer(IndexData indexData) {
        Double latestPrice = indexData.getLast();
        Double lastMonthPrice = indexData.getOneMonthAgoVal();
        if (latestPrice == null || lastMonthPrice == null || lastMonthPrice == 0.0) {
            return Double.MAX_VALUE;
        }
        return ((latestPrice / lastMonthPrice * 100) - 100);
    }

    private Double getYearLowToYearHighDiffPer(IndexData indexData) {
        Double lowestPriceInYear = indexData.getYearLow();
        Double highestPriceInYear = indexData.getYearHigh();
        if (highestPriceInYear == null || lowestPriceInYear == null || lowestPriceInYear == 0.0) {
            return Double.MAX_VALUE;
        }
        return ((highestPriceInYear / lowestPriceInYear * 100) - 100);
    }

    private Double getLatestToYearLowDiffPer(IndexData indexData) {
        Double latestPrice = indexData.getLast();
        Double lowestPriceInYear = indexData.getYearLow();
        if (latestPrice == null || lowestPriceInYear == null || lowestPriceInYear == 0.0) {
            return Double.MAX_VALUE;
        }
        return ((latestPrice / lowestPriceInYear * 100) - 100);
    }

    private void sort(NseAllIndicesResponse allIndices) {
        Collections.sort(allIndices.getData(), (ind1, ind2) -> {
            int diff=Double.compare(ind1.getLatestToYearLowDiffPer(), ind2.getLatestToYearLowDiffPer());
            if(ZERO_D.equals(diff)){
                diff=Double.compare(ind2.getYearLowToYearHighDiffPer(), ind1.getYearLowToYearHighDiffPer());
            }
            if(ZERO_D.equals(diff) && ind1.getPe()!=null && ind2.getPe()!=null){
                diff=Double.compare(ind1.getPe(), ind2.getPe());
            }
            if(ZERO_D.equals(diff) && ind1.getDy()!=null && ind2.getDy()!=null){
                diff=Double.compare(ind2.getDy(), ind1.getDy());
            }
            if(ZERO_D.equals(diff) && ind1.getPb()!=null && ind2.getPb()!=null){
                diff=Double.compare(ind1.getPb(), ind2.getPb());
            }
            if(ZERO_D.equals(diff) && ind1.getAdvances()!=null && ind2.getAdvances()!=null){
                diff=Long.compare(ind2.getAdvances(), ind1.getAdvances());
            }
            if(ZERO_D.equals(diff) && ind1.getDeclines()!=null && ind2.getDeclines()!=null){
                diff=Long.compare(ind1.getDeclines(), ind2.getDeclines());
            }
            if(ZERO_D.equals(diff) && ind1.getUnchanged()!=null && ind2.getUnchanged()!=null){
                diff=Long.compare(ind2.getUnchanged(), ind1.getUnchanged());
            }
            return diff;
        });
    }

    /**
     * Get all NSE indices data using default cookie
     * @return NSE all indices response
     */
    public NseAllIndicesResponse getAllIndices() {
        return process(getAllIndices(null));
    }
    
    /**
     * Get stock quote for a given symbol
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
}
