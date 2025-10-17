package org.example.invest.mapper;

import org.example.invest.dto.IndicesResponse;
import org.example.invest.dto.ProcessDto;
import org.example.invest.dto.bse.index.BseRealTimeData;
import org.example.invest.dto.nse.index.NseAllIndicesResponse;
import org.example.invest.dto.request.RequestBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IndicesResponseMapper {
    @Autowired
    private ProcessDtoMapper processDtoMapper;

    public IndicesResponse getInvestableIndices(RequestBean requestBean) {
        ProcessDto processDto = processDtoMapper.getProcessDto(requestBean);

        return new IndicesResponse(processDto.getNseDto().getInvestableIndices()
                , processDto.getBseDto().getInvestableIndices()
                , getAiOpinion(processDto.getNseDto().getInvestableIndices(), processDto.getBseDto().getInvestableIndices()));
    }

    public Object getAiOpinion(NseAllIndicesResponse nse, List<BseRealTimeData> bse) {
        return null;
    }
}
