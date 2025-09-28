package org.example.invest.controller;

import org.example.invest.dto.NseAllIndicesResponse;
import org.example.invest.dto.NseEtfResponse;
import org.example.invest.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller demonstrating Feign client usage in Java 11
 * This controller exposes endpoints that use the Feign client internally
 */
@RestController
@RequestMapping("/api/stocks")
public class StockController {
    
    @Autowired
    private StockService stockService;
    
    /**
     * Get all NSE indices data with custom cookie
     * @param cookie Cookie header for NSE API authentication
     * @return NSE all indices response
     */
    @GetMapping("/indices")
    public NseAllIndicesResponse getAllIndices(@RequestHeader(value = "Cookie", required = false) String cookie) {
        return stockService.getAllIndices(cookie);
    }


    @GetMapping("/investableIndices")
    public NseAllIndicesResponse getInvestableIndices() {
        return stockService.getInvestableIndices();
    }
    
    /**
     * Get all NSE indices data using default cookie
     * @return NSE all indices response
     */
    @GetMapping("/indices/default")
    public NseAllIndicesResponse getAllIndicesDefault() {
        return stockService.getAllIndices();
    }
    
    /**
     * Get stock quote by symbol
     * @param symbol Stock symbol (e.g., RELIANCE, TCS)
     * @return Stock quote information
     */
    @GetMapping("/quote/{symbol}")
    public String getStockQuote(@PathVariable String symbol) {
        return stockService.getStockQuote(symbol);
    }
    
    /**
     * Get market status
     * @return Current market status
     */
    @GetMapping("/market-status")
    public String getMarketStatus() {
        return stockService.getMarketStatus();
    }
    
    /**
     * Get company information by symbol
     * @param symbol Stock symbol
     * @return Company information
     */
    @GetMapping("/company/{symbol}")
    public String getCompanyInfo(@PathVariable String symbol) {
        return stockService.getCompanyInfo(symbol);
    }
    
    /**
     * Get ETF data from NSE with custom cookie
     * @param cookie Cookie header for NSE API authentication
     * @return NSE ETF response with detailed ETF data
     */
    @GetMapping("/etf")
    public NseEtfResponse getEtfData(@RequestHeader(value = "Cookie", required = false) String cookie
            , @RequestParam(value = "index", required = false) String index) {
        return stockService.getEtfData(cookie, index);
    }
    
    /**
     * Get ETF data from NSE using default cookie
     * @return NSE ETF response with detailed ETF data
     */
    @GetMapping("/etf/default")
    public NseEtfResponse getEtfDataDefault() {
        return stockService.getEtfData();
    }
}