package org.example.invest.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.invest.client.NseClient;
import org.example.invest.dto.nse.etf.EtfData;
import org.example.invest.dto.nse.etf.NseEtfResponse;
import org.example.invest.dto.nse.index.NseAllIndicesResponse;
import org.example.invest.dto.nse.index.NseIndexData;
import org.example.invest.service.NseService;
import org.example.invest.util.GeneralUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;

import static org.example.Constants.HUNDREAD_D;
import static org.example.Constants.ZERO_D;

@Service
@Slf4j
public class NseServiceImpl implements NseService {
    @Autowired
    private NseClient nseClient;

    @Autowired
    private GeneralUtil generalUtil;

    @Override
    public NseAllIndicesResponse getAllIndices() {
        try {
            return process(nseClient.getAllIndices(null));
        } catch (Exception e) {
            log.error("Error fetching NSE indices: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching NSE indices: " + e.getMessage(), e);
        }
    }

    private NseAllIndicesResponse process(NseAllIndicesResponse allIndices) {
        deriveMetrics(allIndices);
        sort(allIndices);
        return allIndices;
    }

    private void deriveMetrics(NseAllIndicesResponse allIndices) {
        if (allIndices == null || CollectionUtils.isEmpty(allIndices.getData())) {
            return;
        }
        for (NseIndexData nseIndexData : allIndices.getData()) {
            nseIndexData.setProcessedIndex(generalUtil.removeHypnUnderScorSpcSecIndAndGetLowerCase(nseIndexData.getIndex()));
            nseIndexData.setProcessedIndexSymbol(generalUtil.removeHypnUnderScorSpcSecIndAndGetLowerCase(nseIndexData.getIndex()));
            nseIndexData.setYearHighToYearLowDiffPer(generalUtil.getDeltaPercent(nseIndexData.getYearHigh(), nseIndexData.getYearLow()));
            nseIndexData.setLatestToYearLowDiffPer(generalUtil.getDeltaPercent(nseIndexData.getLast(), nseIndexData.getYearLow()));
            nseIndexData.setYearHighToLatestDiffPer(generalUtil.getDeltaPercent(nseIndexData.getYearHigh(), nseIndexData.getLast()));
            nseIndexData.setLatestToLastWeekDiffPer(generalUtil.getDeltaPercent(nseIndexData.getLast(), nseIndexData.getOneWeekAgoVal()));
        }
    }

    private void sort(NseAllIndicesResponse allIndices) {
        if (allIndices == null || CollectionUtils.isEmpty(allIndices.getData())) {
            return;
        }
        Collections.sort(allIndices.getData(), (ind1, ind2) -> {
            int diff = Double.compare(ind2.getYearHighToLatestDiffPer(), ind1.getYearHighToLatestDiffPer());
            if (ZERO_D.equals(diff)) {
                diff = Double.compare(ind1.getLatestToYearLowDiffPer(), ind2.getLatestToYearLowDiffPer());
            }
            if (ZERO_D.equals(diff)) {
                diff = Double.compare(ind2.getYearHighToYearLowDiffPer(), ind1.getYearHighToYearLowDiffPer());
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

    @Override
    public NseEtfResponse getAllEtfWithInd(String nseIndiaCookie, NseAllIndicesResponse nseAllIndicesResponse) {
        try {
            NseEtfResponse nseEtfResponse = nseClient.getEtfData(nseIndiaCookie);
            deriveMetricsWithInd(nseEtfResponse, nseAllIndicesResponse);
            sort(nseEtfResponse);
            return nseEtfResponse;
        } catch (Exception e) {
            log.error("Error fetching NSE ETF data: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching NSE ETF data: " + e.getMessage(), e);
        }
    }

    private void deriveMetricsWithInd(NseEtfResponse nseEtfResponse, NseAllIndicesResponse nseAllIndicesResponse) {
        if (nseEtfResponse == null || CollectionUtils.isEmpty(nseEtfResponse.getData())) {
            return;
        }
        for (EtfData etf : nseEtfResponse.getData()) {
            etf.setProcessedAssets(generalUtil.removeHypnUnderScorSpcSecIndAndGetLowerCase(etf.getAssets()));
            etf.setProcessedCompanyName(generalUtil.removeHypnUnderScorSpcSecIndAndGetLowerCase(etf.getCompanyName()));
            etf.setNavToMarketLtPDelta(generalUtil.getDelta(etf.getLtP(), etf.getNav()));
            etf.setNavToMarketLtPDeltaPercent(generalUtil.getDeltaPercent(etf.getNav(), etf.getLtP()));
            etf.setYearHighToYearLowDiffPer(generalUtil.getDeltaPercent(etf.getWkhi(), etf.getWklo()));
            etf.setLatestToYearLowDiffPer(generalUtil.getDeltaPercent(etf.getLtP(), etf.getWklo()));//nearWKL*-1
            setIndDataInEtf(etf, nseAllIndicesResponse);
        }
    }

    private void setIndDataInEtf(EtfData etf, NseAllIndicesResponse nseAllIndicesResponse) {
        if (etf == null || nseAllIndicesResponse == null
                || CollectionUtils.isEmpty(nseAllIndicesResponse.getData())) {
            return;
        }
        for (NseIndexData nseIndexData : nseAllIndicesResponse.getData()) {
            if (isEtfOfProvidedIndex(nseIndexData.getProcessedIndex(), nseIndexData.getProcessedIndexSymbol(), etf)) {
                etf.setNseIndexData(nseIndexData);
                break;
            }
        }
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
                diff = Double.compare(etf1.getYearHighToYearLowDiffPer(), etf2.getYearHighToYearLowDiffPer());
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
}
