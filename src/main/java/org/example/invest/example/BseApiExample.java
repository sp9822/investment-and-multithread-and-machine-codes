package org.example.invest.example;

import org.example.invest.dto.bse.index.BseMktCapBoardResponse;
import org.example.invest.dto.bse.index.BseRealTimeData;
import org.example.invest.service.impl.BseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Example class demonstrating how to use BSE API service
 */
@Component
public class BseApiExample {

    @Autowired
    private BseServiceImpl bseServiceImpl;

    /**
     * Example method to demonstrate BSE API usage
     */
    public void demonstrateBseApiUsage() {
        System.out.println("=== BSE API Usage Examples ===\n");

        // Example 1: Get data for a specific category
        System.out.println("1. Getting data for category 1:");
        try {
            BseMktCapBoardResponse category1Data = bseServiceImpl.getMktCapBoardData(1);
            if (category1Data != null) {
                System.out.println("Real-time data count: " +
                        (category1Data.getRealTime() != null ? category1Data.getRealTime().size() : 0));
                System.out.println("ASON data count: " +
                        (category1Data.getAsOn() != null ? category1Data.getAsOn().size() : 0));
                System.out.println("EOD data count: " +
                        (category1Data.getEod() != null ? category1Data.getEod().size() : 0));
            }
        } catch (Exception e) {
            System.err.println("Error fetching category 1 data: " + e.getMessage());
        }

        // Example 2: Get data for all categories
        System.out.println("\n2. Getting data for all categories (1-5):");
        try {
            Map<Integer, BseMktCapBoardResponse> allData = bseServiceImpl.getAllCategoriesData();
            allData.forEach((category, response) -> {
                if (response != null) {
                    System.out.println("Category " + category + ":");
                    System.out.println("  Real-time: " +
                            (response.getRealTime() != null ? response.getRealTime().size() : 0) + " records");
                    System.out.println("  ASON: " +
                            (response.getAsOn() != null ? response.getAsOn().size() : 0) + " records");
                    System.out.println("  EOD: " +
                            (response.getEod() != null ? response.getEod().size() : 0) + " records");
                } else {
                    System.out.println("Category " + category + ": No data available");
                }
            });
        } catch (Exception e) {
            System.err.println("Error fetching all categories data: " + e.getMessage());
        }

        // Example 3: Get only real-time data
        System.out.println("\n3. Getting only real-time data for all categories:");
        try {
            Map<Integer, List<BseRealTimeData>> realTimeData = bseServiceImpl.getAllRealTimeData();
            realTimeData.forEach((category, data) -> {
                System.out.println("Category " + category + ": " + data.size() + " real-time records");
                if (!data.isEmpty()) {
                    // Show first record as example
                    BseRealTimeData firstRecord = data.get(0);
                    System.out.println("  Example: " + firstRecord.getIndexName() +
                            " (" + firstRecord.getIndexCode() + ") - " + firstRecord.getCurrentValue());
                }
            });
        } catch (Exception e) {
            System.err.println("Error fetching real-time data: " + e.getMessage());
        }

        // Example 4: Get data for specific categories
        System.out.println("\n4. Getting data for specific categories (1, 3, 5):");
        try {
            List<Integer> specificCategories = List.of(1, 3, 5);
            Map<Integer, BseMktCapBoardResponse> specificData = bseServiceImpl.getMktCapBoardDataForCategories(specificCategories);
            specificData.forEach((category, response) -> {
                if (response != null) {
                    System.out.println("Category " + category + ": " +
                            (response.getRealTime() != null ? response.getRealTime().size() : 0) + " real-time records");
                } else {
                    System.out.println("Category " + category + ": No data available");
                }
            });
        } catch (Exception e) {
            System.err.println("Error fetching specific categories data: " + e.getMessage());
        }

        System.out.println("\n=== BSE API Examples Complete ===");
    }

    /**
     * Example method to demonstrate error handling
     */
    public void demonstrateErrorHandling() {
        System.out.println("\n=== BSE API Error Handling Examples ===\n");

        // Example: Invalid category
        System.out.println("1. Testing invalid category (6):");
        try {
            bseServiceImpl.getMktCapBoardData(6);
        } catch (Exception e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        // Example: Invalid category in list
        System.out.println("\n2. Testing invalid categories in list (1, 6, 3):");
        try {
            List<Integer> invalidCategories = List.of(1, 6, 3);
            Map<Integer, BseMktCapBoardResponse> result = bseServiceImpl.getMktCapBoardDataForCategories(invalidCategories);
            result.forEach((category, response) -> {
                if (response != null) {
                    System.out.println("Category " + category + ": Data retrieved successfully");
                } else {
                    System.out.println("Category " + category + ": Failed to retrieve data");
                }
            });
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n=== Error Handling Examples Complete ===");
    }
}
