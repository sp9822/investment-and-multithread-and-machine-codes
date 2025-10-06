package org.example.invest.mapper;

import org.example.invest.dto.BseDto;
import org.example.invest.dto.NseDto;
import org.example.invest.dto.ProcessDto;
import org.example.invest.dto.request.RequestBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProcessDtoMapper {
    @Autowired
    private NseDtoMapper nseDtoMapper;

    @Autowired
    private BseDtoMapper bseDtoMapper;

    public ProcessDto getProcessDto(RequestBean requestBean) {
        ProcessDto processDto = new ProcessDto();

        NseDto nseDto = nseDtoMapper.getNseDto(requestBean);
        processDto.setNseDto(nseDto);

        BseDto bseDto = bseDtoMapper.getBseDto(requestBean);
        processDto.setBseDto(bseDto);

        nseDtoMapper.addAllInvestableEtfAsPerBseIndices(nseDto, bseDto);
        bseDtoMapper.addAllInvestableEtfAsPerNseIndices(nseDto, bseDto);
        return processDto;
    }
}
