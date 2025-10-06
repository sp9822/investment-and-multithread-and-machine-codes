package org.example.invest.mapper;

import org.apache.commons.lang3.StringUtils;
import org.example.invest.dto.bse.BseDto;
import org.example.invest.dto.nse.NseDto;
import org.example.invest.dto.bse.index.BseRealTimeData;
import org.example.invest.dto.nse.etf.EtfData;
import org.example.invest.dto.nse.etf.NseEtfResponse;
import org.example.invest.dto.nse.index.NseAllIndicesResponse;
import org.example.invest.dto.nse.index.NseIndexData;
import org.example.invest.dto.request.RequestBean;
import org.example.invest.service.NseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.Constants.ZERO_D;

@Component
public class NseDtoMapper {
    @Autowired
    private NseService nseService;

    public NseDto getNseDto(RequestBean requestBean) {
        NseDto nseDto = new NseDto();
        nseDto.setNseIndiaCookie(requestBean.getNseIndiaCookie());

        NseAllIndicesResponse nseAllIndicesResponse = nseService.getAllIndices();
        nseDto.setAllIndices(nseAllIndicesResponse);
        NseAllIndicesResponse investableIndices = getInvestableIndices(nseAllIndicesResponse);
        nseDto.setInvestableIndices(investableIndices);

        NseEtfResponse nseEtfResponse = nseService.getAllEtfWithInd(requestBean.getNseIndiaCookie(), nseAllIndicesResponse);
        nseDto.setAllEtf(nseEtfResponse);
        nseDto.setInvestableEtf(getInvestableEtf(nseEtfResponse, investableIndices));
        return nseDto;
    }

    private NseAllIndicesResponse getInvestableIndices(NseAllIndicesResponse nseAllIndicesResponse) {
        if (nseAllIndicesResponse == null) {
            return null;
        }
        return (new NseAllIndicesResponse(getInvestableIndicesData(nseAllIndicesResponse)
                , nseAllIndicesResponse.getTimestamp()
                , nseAllIndicesResponse.getAdvances()
                , nseAllIndicesResponse.getDeclines()
                , nseAllIndicesResponse.getUnchanged()
                , nseAllIndicesResponse.getDates()
        ));
    }

    private List<NseIndexData> getInvestableIndicesData(NseAllIndicesResponse nseAllIndicesResponse) {
        if (nseAllIndicesResponse == null || CollectionUtils.isEmpty(nseAllIndicesResponse.getData())) {
            return null;
        }
        return nseAllIndicesResponse.getData().stream()
                .filter(nseIndexData -> isInvestableNseIndex(nseIndexData))
                .collect(Collectors.toList());
    }

    private boolean isInvestableNseIndex(NseIndexData nseIndexData) {
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

    private NseEtfResponse getInvestableEtf(NseEtfResponse nseEtfResponse, NseAllIndicesResponse investableIndices) {
        if (nseEtfResponse == null || CollectionUtils.isEmpty(nseEtfResponse.getData())) {
            return null;
        }

        return (new NseEtfResponse(nseEtfResponse.getTimestamp()
                , getInvestableEtfData(nseEtfResponse, investableIndices)//, getInvestableEtfData(nseEtfResponse.getData())
                , nseEtfResponse.getAdvances()
                , nseEtfResponse.getDeclines()
                , nseEtfResponse.getUnchanged()
                , nseEtfResponse.getNavDate()
                , nseEtfResponse.getTotalTradedValue()
                , nseEtfResponse.getTotalTradedVolume()
                , nseEtfResponse.getMarketStatus()
        )

        );
    }

    private List<EtfData> getInvestableEtfData(NseEtfResponse nseEtfResponse, NseAllIndicesResponse investableIndices) {
        if (nseEtfResponse == null || CollectionUtils.isEmpty(nseEtfResponse.getData())) {
            return null;
        }
        return nseEtfResponse.getData().stream()
                .filter(etfData -> etfData != null
                        && StringUtils.isNotEmpty(etfData.getDate365dAgo())
                        && etfData.getQty() != null && etfData.getQty() > 20000L //ideally, etfData.getQty() > 20,000L
                        && (isInvestableNseIndex(investableIndices, etfData) || isPerformingEtf(etfData)))
                .collect(Collectors.toList());
    }

    private boolean isInvestableNseIndex(NseAllIndicesResponse investableIndices, EtfData etfData) {
        if (etfData == null || etfData.getNseIndexData() == null
                || investableIndices == null || CollectionUtils.isEmpty(investableIndices.getData())) {
            return false;
        }
        return investableIndices.getData().contains(etfData.getNseIndexData());
    }

    private boolean isPerformingEtf(EtfData etfData) {
        return (etfData != null
                && etfData.getLatestToYearLowDiffPer() != null && etfData.getLatestToYearLowDiffPer() < 12D //ideally, etfData.getLatestToYearLowDiffPer() < 12D
                && etfData.getYearHighToYearLowDiffPer() != null && etfData.getYearHighToYearLowDiffPer() > 10D //Atleast 10% changes in year
                && etfData.getNearWKH() != null && etfData.getNearWKH() > 4D//Makes sure we dont buy at year high
                && etfData.getNavToMarketLtPDeltaPercent() != null && etfData.getNavToMarketLtPDeltaPercent() > -0.1D);//not buying too expensive from NAV
    }

    public void addAllInvestableEtfAsPerBseIndices(NseDto nseDto, BseDto bseDto) {
        if (nseDto == null || nseDto.getAllEtf() == null || CollectionUtils.isEmpty(nseDto.getAllEtf().getData())
                || bseDto == null || CollectionUtils.isEmpty(bseDto.getInvestableIndices())) {
            return;
        }
        if (nseDto.getInvestableEtf() == null) {
            nseDto.setInvestableEtf(new NseEtfResponse());
        }
        if (nseDto.getInvestableEtf().getData() == null) {
            nseDto.getInvestableEtf().setData(new ArrayList<>());
        }
        nseDto.getInvestableEtf().getData().addAll(getAllInvestableEtfAsPerBseIndices(nseDto.getAllEtf().getData()
                , bseDto.getInvestableIndices()));
        nseDto.getInvestableEtf().setData(removeDuplicates(nseDto.getInvestableEtf().getData()));
    }

    private List<EtfData> getAllInvestableEtfAsPerBseIndices(List<EtfData> allEtfDataListdata, List<BseRealTimeData> investableBseIndices) {
        if (CollectionUtils.isEmpty(allEtfDataListdata)) {
            return Collections.EMPTY_LIST;
        }
        return allEtfDataListdata.stream().filter(etfData -> isInvestableEtfAsPerBseIndex(etfData, investableBseIndices))
                .collect(Collectors.toList());

    }

    private boolean isInvestableEtfAsPerBseIndex(EtfData etfData, List<BseRealTimeData> investableBseIndices) {
        if (etfData == null || CollectionUtils.isEmpty(investableBseIndices)) {
            return false;
        }
        for (BseRealTimeData investableBseIndex : investableBseIndices) {
            if (investableBseIndex != null &&
                    (etfData.getProcessedAssets().contains(investableBseIndex.getProcessedIndex())
                            || etfData.getProcessedAssets().contains(investableBseIndex.getProcessedIndexSymbol())
                            || etfData.getProcessedCompanyName().contains(investableBseIndex.getProcessedIndex())
                            || etfData.getProcessedCompanyName().contains(investableBseIndex.getProcessedIndexSymbol()))
            ) {
                etfData.setBseIndexData(investableBseIndex);
                return true;
            }
        }
        return false;
    }

    private List<EtfData> removeDuplicates(List<EtfData> nseEtfDataList) {
        List<EtfData> distBseEtfDataList = new ArrayList<>();
        for (EtfData nseEtfData : nseEtfDataList) {
            if (!distBseEtfDataList.contains(nseEtfData)) {
                distBseEtfDataList.add(nseEtfData);
            }
        }
        return distBseEtfDataList;
    }
}
