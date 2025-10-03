package org.example.invest.controller;

import org.example.invest.dto.nse.etf.NseEtfResponse;
import org.example.invest.dto.nse.index.NseAllIndicesResponse;
import org.example.invest.dto.request.RequestBean;
import org.example.invest.mapper.ProcessDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for BSE (Bombay Stock Exchange) API operations
 */
@RestController
@RequestMapping("/nse")
@CrossOrigin(origins = "*")
public class NseController {
    @Autowired
    private ProcessDtoMapper processDtoMapper;

    @GetMapping("/indices")
    public NseAllIndicesResponse getAllIndices(@RequestHeader(value = "Cookie", required = false) String cookie) {
        return processDtoMapper.getProcessDto(new RequestBean(cookie)).getNseDto().getAllIndices();
    }

    @GetMapping("/investableIndices")
    public NseAllIndicesResponse getInvestableIndices(@RequestHeader(value = "Cookie", required = false) String cookie) {
        return processDtoMapper.getProcessDto(new RequestBean(cookie)).getNseDto().getInvestableIndices();
    }

    @GetMapping("/etfs")
    public NseEtfResponse getAllEtfs(@RequestHeader(value = "Cookie", required = false) String cookie) {
        return processDtoMapper.getProcessDto(new RequestBean(cookie)).getNseDto().getAllEtf();
    }

    @GetMapping("/investableEtfs")
    public NseEtfResponse getInvestableEtfs(@RequestHeader(value = "Cookie", required = false) String cookie) {
        return processDtoMapper.getProcessDto(new RequestBean(cookie)).getNseDto().getInvestableEtf();
    }
}
