package org.example.invest.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.invest.client.BseClient;
import org.example.invest.dto.bse.index.BseMktCapBoardResponse;
import org.example.invest.dto.bse.index.BseAsOnData;
import org.example.invest.dto.bse.index.BseEodData;
import org.example.invest.dto.bse.index.BseRealTimeData;
import org.example.invest.dto.bse.etf.BseEtfResponse;
import org.example.invest.dto.bse.etf.BseEtfData;
import org.example.invest.service.BseService;
import org.example.invest.util.GeneralUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.example.Constants.ZERO_D;

/**
 * Service class for BSE (Bombay Stock Exchange) API operations
 * This service handles market cap board data retrieval for categories 1-5 using RestTemplate
 */
@Service
public class BseServiceImpl implements BseService {
    private static final Integer DEFAULT_TYPE = 2;
    public static final List<Integer> VALID_BSE_INDEX_RETRIEVAL_CATEGORIES = List.of(1, 2, 3, 4, 5);

    @Autowired
    private BseClient bseClient;

    @Autowired
    private GeneralUtil generalUtil;


    public BseMktCapBoardResponse getMktCapBoardData(Integer category, Integer type) {
        try {
            validateCategory(category);
            return bseClient.getMktCapBoardData(category, type);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching BSE market cap board data for category " + category + ": " + e.getMessage(), e);
        }
    }

    /**
     * Get market cap board data for a specific category
     *
     * @param category Category number (1-5)
     * @return BSE market cap board response
     */
    public BseMktCapBoardResponse getMktCapBoardData(Integer category) {
        return getMktCapBoardData(category, DEFAULT_TYPE);
    }

    /**
     * Get market cap board data for all categories (1-5)
     *
     * @return Map containing category as key and response as value
     */
    public Map<Integer, BseMktCapBoardResponse> getAllCategoriesData() {
        Map<Integer, BseMktCapBoardResponse> allData = new HashMap<>();

        for (Integer category = 1; category <= 5; category++) {
            try {
                BseMktCapBoardResponse response = getMktCapBoardData(category);
                allData.put(category, response);
            } catch (Exception e) {
                // Log error but continue with other categories
                System.err.println("Failed to fetch data for category " + category + ": " + e.getMessage());
                allData.put(category, null);
            }
        }

        return allData;
    }

    /**
     * Get only real-time data for all categories
     *
     * @return Map containing category as key and list of real-time data as value
     */
    public Map<Integer, List<BseRealTimeData>> getAllRealTimeData() {
        Map<Integer, BseMktCapBoardResponse> allData = getAllCategoriesData();
        Map<Integer, List<BseRealTimeData>> realTimeData = new HashMap<>();

        allData.forEach((category, response) -> {
            if (response != null && response.getRealTime() != null) {
                realTimeData.put(category, response.getRealTime());
            } else {
                realTimeData.put(category, new ArrayList<>());
            }
        });

        return realTimeData;
    }

    /**
     * Get only ASON data for all categories
     *
     * @return Map containing category as key and list of ASON data as value
     */
    public Map<Integer, List<BseAsOnData>> getAllAsOnData() {
        Map<Integer, BseMktCapBoardResponse> allData = getAllCategoriesData();
        Map<Integer, List<BseAsOnData>> asOnData = new HashMap<>();

        allData.forEach((category, response) -> {
            if (response != null && response.getAsOn() != null) {
                asOnData.put(category, response.getAsOn());
            } else {
                asOnData.put(category, new ArrayList<>());
            }
        });

        return asOnData;
    }

    /**
     * Get only EOD data for all categories
     *
     * @return Map containing category as key and list of EOD data as value
     */
    public Map<Integer, List<BseEodData>> getAllEodData() {
        Map<Integer, BseMktCapBoardResponse> allData = getAllCategoriesData();
        Map<Integer, List<BseEodData>> eodData = new HashMap<>();

        allData.forEach((category, response) -> {
            if (response != null && response.getEod() != null) {
                eodData.put(category, response.getEod());
            } else {
                eodData.put(category, new ArrayList<>());
            }
        });

        return eodData;
    }

    /**
     * Get market cap board data for multiple specific categories
     *
     * @param categories List of category numbers
     * @return Map containing category as key and response as value
     */
    public Map<Integer, BseMktCapBoardResponse> getMktCapBoardDataForCategories(List<Integer> categories) {
        Map<Integer, BseMktCapBoardResponse> result = new HashMap<>();

        for (Integer category : categories) {
            try {
                validateCategory(category);
                BseMktCapBoardResponse response = getMktCapBoardData(category);
                result.put(category, response);
            } catch (Exception e) {
                System.err.println("Failed to fetch data for category " + category + ": " + e.getMessage());
                result.put(category, null);
            }
        }

        return result;
    }

    /**
     * Validate category number
     *
     * @param category Category number to validate
     * @throws IllegalArgumentException if category is not between 1 and 5
     */
    private void validateCategory(Integer category) {
        if (category < 1 || category > 5) {
            throw new IllegalArgumentException("Category must be between 1 and 5, got: " + category);
        }
    }

    @Override
    public List<BseMktCapBoardResponse> getBseMktCapBoardResponseForAllCategories() {
        List<BseMktCapBoardResponse> bseMktCapBoardResponseList = new ArrayList<>();
        for (Integer validBseIndexRetrievalCategory : VALID_BSE_INDEX_RETRIEVAL_CATEGORIES) {
            BseMktCapBoardResponse bseMktCapBoardResponse = getMktCapBoardData(validBseIndexRetrievalCategory);
            derive(bseMktCapBoardResponse);
            bseMktCapBoardResponseList.add(bseMktCapBoardResponse);
        }
        return bseMktCapBoardResponseList;
    }

    @Override
    public List<BseRealTimeData> getInvestableIndices(List<BseMktCapBoardResponse> bseMktCapBoardResponseList) {
        if (CollectionUtils.isEmpty(bseMktCapBoardResponseList)) {
            return null;
        }
        List<BseRealTimeData> bseRealTimeDataList = new ArrayList<>();
        for (BseMktCapBoardResponse bseMktCapBoardResponse : bseMktCapBoardResponseList) {
            bseRealTimeDataList.addAll(getBseRealTimeDataList(bseMktCapBoardResponse));
        }
        sort(bseRealTimeDataList);
        return bseRealTimeDataList;
    }

    private List<BseRealTimeData> getBseRealTimeDataList(BseMktCapBoardResponse bseMktCapBoardResponse) {
        if (bseMktCapBoardResponse == null || CollectionUtils.isEmpty(bseMktCapBoardResponse.getRealTime())) {
            return Collections.emptyList();
        }
        return bseMktCapBoardResponse.getRealTime().stream()
                .filter(bseIndexData -> isInvestableIndex(bseIndexData))
                .collect(Collectors.toList());
    }

    private boolean isInvestableIndex(BseRealTimeData bseRealTimeData) {
        return (bseRealTimeData != null
                //&& StringUtils.isNotEmpty(bseRealTimeData.getDate365dAgo())
                && bseRealTimeData.getWeek52Low() != null && bseRealTimeData.getWeek52Low() > ZERO_D
                && bseRealTimeData.getWeek52High() != null && bseRealTimeData.getWeek52High() > ZERO_D
                //&& bseRealTimeData.getOneYearAgoVal() != null && bseRealTimeData.getOneYearAgoVal() > ZERO_D
                //&& bseRealTimeData.getWeek52High() > bseRealTimeData.getOneYearAgoVal()
                && bseRealTimeData.getLatestToYearLowDiffPer() != null && bseRealTimeData.getLatestToYearLowDiffPer() < 12D//Ideally bseRealTimeData.getLatestToYearLowDiffPer() < 12D
                && bseRealTimeData.getYearHighToYearLowDiffPer() != null && bseRealTimeData.getYearHighToYearLowDiffPer() > 10D //Yearly 10% change
                && bseRealTimeData.getYearHighToLatestDiffPer() != null && bseRealTimeData.getYearHighToLatestDiffPer() > 4D//Makes sure we dont buy at year high
                && filterOutIndexAsPerPePbDy(bseRealTimeData)
        );
    }

    private boolean filterOutIndexAsPerPePbDy(BseRealTimeData bseRealTimeData) {
        bseClient.setFundamentals(bseRealTimeData);
        return true;
        //&& bseRealTimeData.getPe() != null && bseRealTimeData.getPe() <= 22D
        //&& bseRealTimeData.getPb() != null && bseRealTimeData.getPb() <= 3.5D
        //&& bseRealTimeData.getDy() != null && bseRealTimeData.getDy() >= 1.2D
    }

    private void derive(BseMktCapBoardResponse bseMktCapBoardResponse) {
        if (bseMktCapBoardResponse == null) {
            return;
        }

        List<BseRealTimeData> bseIndicesRealTimeData = bseMktCapBoardResponse.getRealTime();
        if (CollectionUtils.isNotEmpty(bseIndicesRealTimeData)) {
            for (BseRealTimeData bseIndexRealTimeData : bseIndicesRealTimeData) {
                derive(bseIndexRealTimeData);
            }
            sort(bseIndicesRealTimeData);
        }
    }

    private void derive(BseRealTimeData bseIndexRealTimeData) {
        if (bseIndexRealTimeData == null) {
            return;
        }
        bseIndexRealTimeData.setProcessedIndex(generalUtil.removeHypnUnderScorSpcSecIndAndGetLowerCase(bseIndexRealTimeData.getIndexName()));
        bseIndexRealTimeData.setProcessedIndexSymbol(generalUtil.removeHypnUnderScorSpcSecIndAndGetLowerCase(bseIndexRealTimeData.getIndexCode()));
        bseIndexRealTimeData.setYearHighToLatestDiffPer(generalUtil.getDeltaPercent(bseIndexRealTimeData.getWeek52High()
                , bseIndexRealTimeData.getCurrentValue()));
        bseIndexRealTimeData.setLatestToYearLowDiffPer(generalUtil.getDeltaPercent(bseIndexRealTimeData.getCurrentValue()
                , bseIndexRealTimeData.getWeek52Low()));
        bseIndexRealTimeData.setYearHighToYearLowDiffPer(generalUtil.getDeltaPercent(bseIndexRealTimeData.getWeek52High()
                , bseIndexRealTimeData.getWeek52Low()));
    }

    private void sort(List<BseRealTimeData> bseIndicesRealTimeData) {
        Collections.sort(bseIndicesRealTimeData, (ind1, ind2) -> {
            int diff = Double.compare(ind1.getLatestToYearLowDiffPer(), ind2.getLatestToYearLowDiffPer());
            if (ZERO_D.equals(diff)) {
                diff = Double.compare(ind2.getYearHighToLatestDiffPer(), ind1.getYearHighToLatestDiffPer());
            }
            if (ZERO_D.equals(diff)) {
                diff = Double.compare(ind2.getYearHighToYearLowDiffPer(), ind1.getYearHighToYearLowDiffPer());
            }
            /*
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
            */
            return diff;
        });
    }

    @Override
    public BseEtfResponse getAllEtfWithInd(List<BseMktCapBoardResponse> bseAllIndicesResponse) {
        try {
            BseEtfResponse bseEtfResponse = bseClient.getEtfData();
            deriveMetricsWithInd(bseEtfResponse, bseAllIndicesResponse);
            sort(bseEtfResponse);
            return bseEtfResponse;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching all BSE ETF data: " + e.getMessage(), e);
        }
    }

    private void deriveMetricsWithInd(BseEtfResponse bseEtfResponse, List<BseMktCapBoardResponse> bseAllIndicesResponse) {
        if (bseEtfResponse == null || CollectionUtils.isEmpty(bseEtfResponse.getData())) {
            return;
        }
        for (BseEtfData etf : bseEtfResponse.getData()) {
            etf.setNavToMarketLtPDelta(generalUtil.getDelta(etf.getLtp(), etf.getNav()));
            etf.setNavToMarketLtPDeltaPercent(generalUtil.getDeltaPercent(etf.getNav(), etf.getLtp()));
            etf.setYearHighToLatestDiffPer(generalUtil.getDeltaPercent(etf.getWeek52High(), etf.getLtp()));
            etf.setYearHighToYearLowDiffPer(generalUtil.getDeltaPercent(etf.getWeek52High(), etf.getWeek52Low()));
            etf.setLatestToYearLowDiffPer(generalUtil.getDeltaPercent(etf.getLtp(), etf.getWeek52Low()));//nearWKL*-1
            setIndDataInEtf(etf, bseAllIndicesResponse);
        }
    }

    private void setIndDataInEtf(BseEtfData etf, List<BseMktCapBoardResponse> bseAllIndicesResponse) {
        if (etf == null || bseAllIndicesResponse == null
                || CollectionUtils.isEmpty(bseAllIndicesResponse)) {
            return;
        }
        for (BseMktCapBoardResponse bseMktCapBoardResponse : bseAllIndicesResponse) {
            for (BseRealTimeData bseRealTimeData : bseMktCapBoardResponse.getRealTime()) {
                if (isEtfOfProvidedIndex(bseRealTimeData.getProcessedIndex(), bseRealTimeData.getProcessedIndexSymbol(), etf)) {
                    etf.setBseIndexData(bseRealTimeData);
                    return;
                }
            }
        }
    }

    private boolean isEtfOfProvidedIndex(String indexSubStr, String indexSymbolSubStr, BseEtfData etf) {
        if (StringUtils.isEmpty(etf.getProcessedScripCode()) && StringUtils.isEmpty(etf.getProcessedScripName())) {
            return false;
        }
        if (StringUtils.isEmpty(indexSubStr) && StringUtils.isEmpty(indexSymbolSubStr)) {
            return false;
        }
        return (
                (StringUtils.isNotEmpty(etf.getProcessedScripCode())
                        && (etf.getProcessedScripCode().contains(indexSubStr) || etf.getProcessedScripCode().contains(indexSymbolSubStr)))
                        || (StringUtils.isNotEmpty(etf.getProcessedScripName())
                        && (etf.getProcessedScripName().contains(indexSubStr) || etf.getProcessedScripName().contains(indexSymbolSubStr)))
                        || (StringUtils.isNotEmpty(indexSubStr)
                        && (indexSubStr.contains(etf.getProcessedScripCode()) || indexSubStr.contains(etf.getProcessedScripName())))
                        || (StringUtils.isNotEmpty(indexSymbolSubStr)
                        && (indexSymbolSubStr.contains(etf.getProcessedScripCode()) || indexSymbolSubStr.contains(etf.getProcessedScripName())))
        );
    }

    private void sort(BseEtfResponse etfData) {
        if (etfData == null || CollectionUtils.isEmpty(etfData.getData())) {
            return;
        }
        Collections.sort(etfData.getData(), (etf1, etf2) -> {
            int diff = Double.compare(etf2.getNavToMarketLtPDeltaPercent(), etf1.getNavToMarketLtPDeltaPercent());
            if (ZERO_D.equals(diff)) {
                diff = Double.compare(etf1.getLatestToYearLowDiffPer(), etf2.getLatestToYearLowDiffPer());
            }
            if (ZERO_D.equals(diff)) {
                diff = Double.compare(etf1.getYearHighToYearLowDiffPer(), etf2.getYearHighToYearLowDiffPer());
            }
            if (ZERO_D.equals(diff) && etf1.getVolume() != null && etf2.getVolume() != null) {
                diff = Double.compare(etf2.getVolume(), etf1.getVolume());
            }
            if (ZERO_D.equals(diff) && etf1.getChangePercent() != null && etf2.getChangePercent() != null) {
                diff = Double.compare(etf1.getChangePercent(), etf2.getChangePercent());
            }
            return diff;
        });
    }

    @Override
    public BseEtfResponse getInvestableEtf(BseEtfResponse bseEtfResponse, List<BseRealTimeData> investableIndices) {
        if (bseEtfResponse == null || CollectionUtils.isEmpty(bseEtfResponse.getData())) {
            return bseEtfResponse;
        }

        // Filter ETFs based on investability criteria
        List<BseEtfData> investableEtfs = bseEtfResponse.getData().stream()
                .filter(etfData -> etfData != null
                                && etfData.getVolume() != null && etfData.getVolume() > 20000L //ideally, etfData.getVolume() > 20,000L
                                && (isInvestableBseIndex(investableIndices, etfData) || isPerformingEtf(etfData))
                                && etfData.getLtp() != null && etfData.getWtAvgPrice() != null
                        //&& etfData.getLtp() <= etfData.getWtAvgPrice()
                )
                .collect(Collectors.toList());

        // Create response with filtered data
        BseEtfResponse investableResponse = new BseEtfResponse(bseEtfResponse.getTimestamp()
                , investableEtfs
                , investableEtfs.size()
                , ((int) investableEtfs.stream().filter(etf -> etf.getChange() != null && etf.getChange() > 0).count())
                , ((int) investableEtfs.stream().filter(etf -> etf.getChange() != null && etf.getChange() < 0).count())
                , ((int) investableEtfs.stream().filter(etf -> etf.getChange() != null && etf.getChange() == 0).count())
                , (investableEtfs.stream().mapToDouble(etf -> etf.getTurnover() != null ? etf.getTurnover() : 0.0).sum())
                , (investableEtfs.stream().mapToLong(etf -> etf.getVolume() != null ? etf.getVolume() : 0L).sum())
                , (investableEtfs.stream().mapToDouble(etf -> etf.getMarketCap() != null ? etf.getMarketCap() : 0.0).sum())
                , bseEtfResponse.getMarketStatus()
                , bseEtfResponse.getLastUpdated()
                , bseEtfResponse.getSource()
                , bseEtfResponse.getSuccess()
                , "Investable ETF data retrieved successfully"
                , "NA");

        return investableResponse;
    }

    private boolean isInvestableBseIndex(List<BseRealTimeData> investableIndices, BseEtfData etfData) {
        if (etfData == null || etfData.getBseIndexData() == null || CollectionUtils.isEmpty(investableIndices)) {
            return false;
        }
        return investableIndices.contains(etfData.getBseIndexData());
    }

    /**
     * Check if an ETF is investable based on certain criteria
     *
     * @param etfData ETF data to check
     * @return true if ETF is investable, false otherwise
     */
    private boolean isPerformingEtf(BseEtfData etfData) {
        // Basic criteria for investable ETFs
        return (etfData != null
                && etfData.getLatestToYearLowDiffPer() != null && etfData.getLatestToYearLowDiffPer() < 12D //ideally, etfData.getLatestToYearLowDiffPer() < 12D
                && etfData.getYearHighToYearLowDiffPer() != null && etfData.getYearHighToYearLowDiffPer() > 10D //Atleast 10% changes in year
                && etfData.getYearHighToLatestDiffPer() != null && etfData.getYearHighToLatestDiffPer() > 4D//Makes sure we dont buy at year high
                //&& etfData.getNavToMarketLtPDeltaPercent() != null && etfData.getNavToMarketLtPDeltaPercent() > -0.1D
                //&& (etfData.getIsActive() != null || etfData.getIsActive())
                //&& !Boolean.TRUE.equals(etfData.getIsSuspended())
                //&& !Boolean.TRUE.equals(etfData.getIsDelisted())
                && etfData.getTurnover() != null && etfData.getTurnover() > 0
                //&& etfData.getMarketCap() != null && etfData.getMarketCap() > 1000000
        ); // Minimum market cap of 1M
    }
}