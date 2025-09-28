package org.example.invest.client;

import org.example.invest.dto.NseAllIndicesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign client interface for NSE (National Stock Exchange) API
 * This is an example of how to create a Feign client in Java 11 with Spring Boot
 */
@FeignClient(name = "nse-client", url = "https://www.nseindia.com/api", configuration = org.example.invest.config.FeignConfig.class)
public interface NseClient {
    
    /**
     * Get all NSE indices data
     * @param cookie Cookie header for NSE API authentication
     * @return NSE all indices response with detailed index data
     */
    @GetMapping("/allIndices")
    NseAllIndicesResponse getAllIndices(@RequestHeader("Cookie") String cookie);
    
    /**
     * Example method to get stock quote information
     * @param symbol Stock symbol (e.g., "RELIANCE", "TCS")
     * @return Stock quote data as String (you can create proper DTOs)
     */
    @GetMapping("/quote-equity")
    String getStockQuote(@RequestParam("symbol") String symbol);
    
    /**
     * Example method to get market status
     * @return Market status information
     */
    @GetMapping("/marketStatus")
    String getMarketStatus();
    
    /**
     * Example method to get company information by symbol
     * @param symbol Stock symbol
     * @return Company information
     */
    @GetMapping("/company/{symbol}")
    String getCompanyInfo(@PathVariable("symbol") String symbol);
}
