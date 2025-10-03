package org.example.invest.example;

import org.example.invest.dto.nse.etf.NseEtfResponse;
import org.example.invest.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Example class demonstrating how to use the NSE ETF API
 * This class shows practical usage of the ETF API integration
 */
@Component
public class NseEtfApiExample {

    @Autowired
    private StockService stockService;

    /**
     * Example method to demonstrate NSE ETF API usage
     */
    public void demonstrateEtfApiUsage() {
        try {
            System.out.println("=== NSE ETF API Demo ===");

            // Get ETF data using default cookie
            NseEtfResponse response = stockService.getEtfData();

            System.out.println("Timestamp: " + response.getTimestamp());
            System.out.println("Total ETFs: " + (response.getData() != null ? response.getData().size() : 0));
            System.out.println("Market Summary:");
            System.out.println("  Advances: " + response.getAdvances());
            System.out.println("  Declines: " + response.getDeclines());
            System.out.println("  Unchanged: " + response.getUnchanged());
            System.out.println("  Total Traded Value: " + response.getTotalTradedValue());
            System.out.println("  Total Traded Volume: " + response.getTotalTradedVolume());
            System.out.println("  NAV Date: " + response.getNavDate());

            // Display market status
            if (response.getMarketStatus() != null) {
                System.out.println("\nMarket Status:");
                System.out.println("  Market: " + response.getMarketStatus().getMarket());
                System.out.println("  Status: " + response.getMarketStatus().getMarketStatus());
                System.out.println("  Trade Date: " + response.getMarketStatus().getTradeDate());
                System.out.println("  Index: " + response.getMarketStatus().getIndex());
                System.out.println("  Last: " + response.getMarketStatus().getLast());
                System.out.println("  Variation: " + response.getMarketStatus().getVariation());
                System.out.println("  Percent Change: " + response.getMarketStatus().getPercentChange());
                System.out.println("  Message: " + response.getMarketStatus().getMarketStatusMessage());
            }

            // Display top 5 ETFs
            System.out.println("\nTop 5 ETFs:");
            if (response.getData() != null) {
                response.getData().stream()
                        .limit(5)
                        .forEach(etf -> {
                            System.out.println(String.format(
                                    "  %s (%s): %s (%.2f%%)",
                                    etf.getSymbol(),
                                    etf.getAssets(),
                                    etf.getLtP(),
                                    etf.getPerChange365d() != null ? etf.getPerChange365d() : 0.0
                            ));
                        });
            }

            // Find specific ETF by symbol
            String targetSymbol = "MONQ50";
            if (response.getData() != null) {
                response.getData().stream()
                        .filter(etf -> targetSymbol.equals(etf.getSymbol()))
                        .findFirst()
                        .ifPresent(etf -> {
                            System.out.println("\nSpecific ETF Details for " + targetSymbol + ":");
                            if (etf.getMeta() != null) {
                                System.out.println("  Company: " + etf.getMeta().getCompanyName());
                                System.out.println("  Industry: " + etf.getMeta().getIndustry());
                                System.out.println("  ISIN: " + etf.getMeta().getIsin());
                                System.out.println("  Listing Date: " + etf.getMeta().getListingDate());
                            }
                            System.out.println("  Current Price: " + etf.getLtP());
                            System.out.println("  NAV: " + etf.getNav());
                            System.out.println("  365d Change: " + etf.getPerChange365d() + "%");
                            System.out.println("  30d Change: " + etf.getPerChange30d() + "%");
                        });
            }

        } catch (Exception e) {
            System.err.println("Error demonstrating ETF API usage: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Example method to demonstrate ETF API usage with custom cookie
     */
    public void demonstrateEtfApiUsageWithCookie(String cookie) {
        try {
            System.out.println("=== NSE ETF API Demo with Custom Cookie ===");

            // Get ETF data using custom cookie
            NseEtfResponse response = stockService.getEtfData(cookie, null, null);

            System.out.println("Timestamp: " + response.getTimestamp());
            System.out.println("Total ETFs: " + (response.getData() != null ? response.getData().size() : 0));

            // Display ETF symbols
            System.out.println("\nETF Symbols:");
            if (response.getData() != null) {
                response.getData().stream()
                        .map(etf -> etf.getSymbol())
                        .forEach(symbol -> System.out.println("  " + symbol));
            }

        } catch (Exception e) {
            System.err.println("Error demonstrating ETF API usage with cookie: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

