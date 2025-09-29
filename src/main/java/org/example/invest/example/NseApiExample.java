package org.example.invest.example;

import org.example.invest.dto.NseAllIndicesResponse;
import org.example.invest.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Example class demonstrating how to use the NSE AllIndices API
 * This class shows practical usage of the Feign client integration
 */
@Component
public class NseApiExample {

    @Autowired
    private StockService stockService;

    /**
     * Example method to demonstrate NSE AllIndices API usage
     */
    public void demonstrateNseApiUsage() {
        try {
            System.out.println("=== NSE AllIndices API Demo ===");

            // Get all indices using default cookie
            NseAllIndicesResponse response = stockService.getAllIndices();

            System.out.println("Timestamp: " + response.getTimestamp());
            System.out.println("Total Indices: " + (response.getData() != null ? response.getData().size() : 0));
            System.out.println("Market Summary:");
            System.out.println("  Advances: " + response.getAdvances());
            System.out.println("  Declines: " + response.getDeclines());
            System.out.println("  Unchanged: " + response.getUnchanged());

            // Display top 5 indices
            System.out.println("\nTop 5 Indices:");
            if (response.getData() != null) {
                response.getData().stream()
                        .limit(5)
                        .forEach(index -> {
                            System.out.println(String.format(
                                    "  %s (%s): %.2f (%.2f%%)",
                                    index.getIndex(),
                                    index.getIndexSymbol(),
                                    index.getLast(),
                                    index.getPercentChange()
                            ));
                        });
            }

            // Find NIFTY 50 specifically
            if (response.getData() != null) {
                response.getData().stream()
                        .filter(index -> "NIFTY 50".equals(index.getIndex()))
                        .findFirst()
                        .ifPresent(nifty50 -> {
                            System.out.println("\nNIFTY 50 Details:");
                            System.out.println("  Current: " + nifty50.getLast());
                            System.out.println("  Change: " + nifty50.getVariation() + " (" + nifty50.getPercentChange() + "%)");
                            System.out.println("  Open: " + nifty50.getOpen());
                            System.out.println("  High: " + nifty50.getHigh());
                            System.out.println("  Low: " + nifty50.getLow());
                            System.out.println("  PE Ratio: " + nifty50.getPe());
                            System.out.println("  PB Ratio: " + nifty50.getPb());
                            System.out.println("  Dividend Yield: " + nifty50.getDy());
                        });
            }

        } catch (Exception e) {
            System.err.println("Error fetching NSE data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Example method to demonstrate filtering indices by category
     */
    public void demonstrateIndexFiltering() {
        try {
            NseAllIndicesResponse response = stockService.getAllIndices();

            System.out.println("\n=== Index Filtering Demo ===");

            // Filter by derivatives eligible indices
            System.out.println("Derivatives Eligible Indices:");
            if (response.getData() != null) {
                response.getData().stream()
                        .filter(index -> "INDICES ELIGIBLE IN DERIVATIVES".equals(index.getKey()))
                        .forEach(index -> {
                            System.out.println(String.format(
                                    "  %s: %.2f (%.2f%%)",
                                    index.getIndex(),
                                    index.getLast(),
                                    index.getPercentChange()
                            ));
                        });
            }

            // Filter by sectoral indices
            System.out.println("\nSectoral Indices:");
            if (response.getData() != null) {
                response.getData().stream()
                        .filter(index -> "SECTORAL INDICES".equals(index.getKey()))
                        .limit(5)
                        .forEach(index -> {
                            System.out.println(String.format(
                                    "  %s: %.2f (%.2f%%)",
                                    index.getIndex(),
                                    index.getLast(),
                                    index.getPercentChange()
                            ));
                        });
            }

        } catch (Exception e) {
            System.err.println("Error filtering indices: " + e.getMessage());
        }
    }
}


