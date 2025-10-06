package org.example.invest.controller;

import org.example.invest.dto.bse.index.BseMktCapBoardResponse;
import org.example.invest.dto.bse.index.BseAsOnData;
import org.example.invest.dto.bse.index.BseEodData;
import org.example.invest.dto.bse.index.BseRealTimeData;
import org.example.invest.dto.bse.etf.BseEtfResponse;
import org.example.invest.dto.request.RequestBean;
import org.example.invest.mapper.ProcessDtoMapper;
import org.example.invest.service.impl.BseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for BSE (Bombay Stock Exchange) API operations
 */
@RestController
@RequestMapping("/bse")
@CrossOrigin(origins = "*")
public class BseController {
    @Autowired
    private ProcessDtoMapper processDtoMapper;

    @Autowired
    private BseServiceImpl bseServiceImpl;

    /**
     * Get market cap board data for a specific category
     *
     * @param category Category number (1-5)
     * @return BSE market cap board response
     */
    @GetMapping("/mkt-cap-board/{category}")
    public ResponseEntity<BseMktCapBoardResponse> getMktCapBoardData(@PathVariable int category) {
        try {
            BseMktCapBoardResponse response = bseServiceImpl.getMktCapBoardData(category);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get market cap board data for a specific category with custom type
     *
     * @param category Category number (1-5)
     * @param type     Type parameter
     * @return BSE market cap board response
     */
    @GetMapping("/mkt-cap-board/{category}/{type}")
    public ResponseEntity<BseMktCapBoardResponse> getMktCapBoardDataWithType(
            @PathVariable int category,
            @PathVariable int type) {
        try {
            BseMktCapBoardResponse response = bseServiceImpl.getMktCapBoardData(category, type);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get market cap board data for all categories (1-5)
     *
     * @return Map containing category as key and response as value
     */
    @GetMapping("/mkt-cap-board/all")
    public ResponseEntity<Map<Integer, BseMktCapBoardResponse>> getAllCategoriesData() {
        try {
            Map<Integer, BseMktCapBoardResponse> response = bseServiceImpl.getAllCategoriesData();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get only real-time data for all categories
     *
     * @return Map containing category as key and list of real-time data as value
     */
    @GetMapping("/real-time/all")
    public ResponseEntity<Map<Integer, List<BseRealTimeData>>> getAllRealTimeData() {
        try {
            Map<Integer, List<BseRealTimeData>> response = bseServiceImpl.getAllRealTimeData();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get only ASON data for all categories
     *
     * @return Map containing category as key and list of ASON data as value
     */
    @GetMapping("/as-on/all")
    public ResponseEntity<Map<Integer, List<BseAsOnData>>> getAllAsOnData() {
        try {
            Map<Integer, List<BseAsOnData>> response = bseServiceImpl.getAllAsOnData();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get only EOD data for all categories
     *
     * @return Map containing category as key and list of EOD data as value
     */
    @GetMapping("/eod/all")
    public ResponseEntity<Map<Integer, List<BseEodData>>> getAllEodData() {
        try {
            Map<Integer, List<BseEodData>> response = bseServiceImpl.getAllEodData();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get market cap board data for specific categories
     *
     * @param categories Comma-separated list of category numbers
     * @return Map containing category as key and response as value
     */
    @GetMapping("/mkt-cap-board/categories")
    public ResponseEntity<Map<Integer, BseMktCapBoardResponse>> getMktCapBoardDataForCategories(
            @RequestParam List<Integer> categories) {
        try {

            Map<Integer, BseMktCapBoardResponse> response = bseServiceImpl.getMktCapBoardDataForCategories(categories);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/indices")
    public List<BseMktCapBoardResponse> getAllIndices(@RequestHeader(value = "Cookie", required = false) String cookie) {
        return processDtoMapper.getProcessDto(new RequestBean(cookie)).getBseDto().getBseMktCapBoardResponseForAllCategories();
    }

    @GetMapping("/investableIndices")
    public List<BseRealTimeData> getInvestableIndices(@RequestHeader(value = "Cookie", required = false) String cookie) {
        return processDtoMapper.getProcessDto(new RequestBean(cookie)).getBseDto().getInvestableIndices();
    }

    @GetMapping("/etfs")
    public BseEtfResponse getAllEtfs(@RequestHeader(value = "Cookie", required = false) String cookie) {
        return processDtoMapper.getProcessDto(new RequestBean(cookie)).getBseDto().getAllEtf();
    }

    @GetMapping("/investableEtfs")
    public BseEtfResponse getInvestableEtfs(@RequestHeader(value = "Cookie", required = false) String cookie) {
        return processDtoMapper.getProcessDto(new RequestBean(cookie)).getBseDto().getInvestableEtf();
    }
}