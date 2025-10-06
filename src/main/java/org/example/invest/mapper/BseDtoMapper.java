package org.example.invest.mapper;

import org.apache.commons.lang3.StringUtils;
import org.example.invest.dto.bse.BseDto;
import org.example.invest.dto.nse.NseDto;
import org.example.invest.dto.bse.etf.BseEtfData;
import org.example.invest.dto.bse.etf.BseEtfResponse;
import org.example.invest.dto.bse.index.BseMktCapBoardResponse;
import org.example.invest.dto.bse.index.BseRealTimeData;
import org.example.invest.dto.nse.index.NseAllIndicesResponse;
import org.example.invest.dto.nse.index.NseIndexData;
import org.example.invest.dto.request.RequestBean;
import org.example.invest.service.BseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BseDtoMapper {
    @Autowired
    private BseService bseService;

    public BseDto getBseDto(RequestBean requestBean) {
        BseDto bseDto = new BseDto();
        List<BseMktCapBoardResponse> bseMktCapBoardResponseList = bseService.getBseMktCapBoardResponseForAllCategories();
        bseDto.setBseMktCapBoardResponseForAllCategories(bseMktCapBoardResponseList);
        List<BseRealTimeData> investableIndices = bseService.getInvestableIndices(bseMktCapBoardResponseList);
        bseDto.setInvestableIndices(investableIndices);

        // Set ETF data
        BseEtfResponse bseEtfResponse = bseService.getAllEtfWithInd(bseMktCapBoardResponseList);
        bseDto.setAllEtf(bseEtfResponse);
        bseDto.setInvestableEtf(bseService.getInvestableEtf(bseEtfResponse, investableIndices));
        return bseDto;
    }

    public void addAllInvestableEtfAsPerNseIndices(NseDto nseDto, BseDto bseDto) {
        if (bseDto == null || bseDto.getAllEtf() == null || CollectionUtils.isEmpty(bseDto.getAllEtf().getData())
                || nseDto == null || nseDto.getInvestableIndices() == null
                || CollectionUtils.isEmpty(nseDto.getInvestableIndices().getData())) {
            return;
        }
        if (bseDto.getInvestableEtf() == null) {
            bseDto.setInvestableEtf(new BseEtfResponse());
        }
        if (bseDto.getInvestableEtf().getData() == null) {
            bseDto.getInvestableEtf().setData(new ArrayList<>());
        }
        bseDto.getInvestableEtf().getData().addAll(getAllInvestableEtfAsPerBseIndices(bseDto.getAllEtf().getData()
                , nseDto.getInvestableIndices()));
        bseDto.getInvestableEtf().setData(removeDuplicates(bseDto.getInvestableEtf().getData()));
    }

    private List<BseEtfData> getAllInvestableEtfAsPerBseIndices(List<BseEtfData> allEtfDataListdata, NseAllIndicesResponse investableNseIndices) {
        if (CollectionUtils.isEmpty(allEtfDataListdata)) {
            return Collections.EMPTY_LIST;
        }
        return allEtfDataListdata.stream().filter(etfData -> isInvestableEtfAsPerBseIndex(etfData, investableNseIndices))
                .collect(Collectors.toList());
    }

    private boolean isInvestableEtfAsPerBseIndex(BseEtfData etfData, NseAllIndicesResponse investableNseIndices) {
        if (etfData == null || investableNseIndices == null || CollectionUtils.isEmpty(investableNseIndices.getData())) {
            return false;
        }
        boolean investable = false;
        for (NseIndexData investableNseIndex : investableNseIndices.getData()) {
            if (investableNseIndex != null) {
                if (StringUtils.isNotEmpty(etfData.getProcessedScripCode())) {
                    investable = etfData.getProcessedScripCode().contains(investableNseIndex.getProcessedIndex())
                            || etfData.getProcessedScripCode().contains(investableNseIndex.getProcessedIndexSymbol());
                }
                if (StringUtils.isNotEmpty(etfData.getProcessedScripName())) {
                    investable = investable || etfData.getProcessedScripName().contains(investableNseIndex.getProcessedIndex())
                            || etfData.getProcessedScripName().contains(investableNseIndex.getProcessedIndexSymbol());
                }
                if (investable) {
                    etfData.setNseIndexData(investableNseIndex);
                    return true;
                }
            }
        }
        return false;
    }

    private List<BseEtfData> removeDuplicates(List<BseEtfData> bseEtfDataList) {
        List<BseEtfData> distBseEtfDataList = new ArrayList<>();
        for (BseEtfData bseEtfData : bseEtfDataList) {
            if (!distBseEtfDataList.contains(bseEtfData)) {
                distBseEtfDataList.add(bseEtfData);
            }
        }
        return distBseEtfDataList;
    }
}
