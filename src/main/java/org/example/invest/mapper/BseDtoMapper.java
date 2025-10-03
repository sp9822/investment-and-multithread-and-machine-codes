package org.example.invest.mapper;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.invest.dto.BseDto;
import org.example.invest.dto.bse.index.BseMktCapBoardResponse;
import org.example.invest.dto.bse.index.BseRealTimeData;
import org.example.invest.dto.nse.index.NseIndexData;
import org.example.invest.dto.request.RequestBean;
import org.example.invest.service.BseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.Constants.ZERO_D;

@Component
public class BseDtoMapper {
    @Autowired
    private BseService bseService;

    public BseDto getBseDto(RequestBean requestBean) {
        BseDto bseDto = new BseDto();
        List<BseMktCapBoardResponse> bseMktCapBoardResponseList = bseService.getBseMktCapBoardResponseForAllCategories();
        bseDto.setBseMktCapBoardResponseForAllCategories(bseMktCapBoardResponseList);
        bseDto.setInvestableIndices(bseService.getInvestableIndices(bseMktCapBoardResponseList));

        //getEtfs
        //getInvestableEtfs
        return bseDto;
    }
}
