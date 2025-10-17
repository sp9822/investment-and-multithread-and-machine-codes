package org.example.invest.mapper;

import org.example.invest.dto.EtfResponse;
import org.example.invest.dto.ProcessDto;
import org.example.invest.dto.bse.etf.BseEtfResponse;
import org.example.invest.dto.nse.etf.NseEtfResponse;
import org.example.invest.dto.request.RequestBean;
import org.example.invest.service.AiAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EtfResponseMapper {
    @Autowired
    private ProcessDtoMapper processDtoMapper;

    @Autowired
    private AiAnalysisService aiAnalysisService;

    public EtfResponse getInvestableEtfs(RequestBean requestBean) {
        ProcessDto processDto = processDtoMapper.getProcessDto(requestBean);
        return new EtfResponse(processDto.getNseDto().getInvestableEtf()
                , processDto.getBseDto().getInvestableEtf()
                , getAiOpinion(processDto.getNseDto().getInvestableEtf(), processDto.getBseDto().getInvestableEtf()));
    }

    public Object getAiOpinion(NseEtfResponse nse, BseEtfResponse bse) {
        try {
            return aiAnalysisService.getAiOpinion(nse, bse);
        } catch (Exception e) {
            // Log the error and return a fallback response
            return "AI analysis temporarily unavailable. Please try again later.";
        }
    }
}