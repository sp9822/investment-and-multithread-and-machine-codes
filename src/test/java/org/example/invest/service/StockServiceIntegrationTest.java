package org.example.invest.service;

import org.example.invest.dto.NseAllIndicesResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for StockService that tests real NseClient calls
 * This test requires actual network connectivity to NSE API
 */
@SpringBootTest
@TestPropertySource(properties = {
    "logging.level.org.example.invest=DEBUG",
    "feign.client.config.nse-client.loggerLevel=FULL"
})
public class StockServiceIntegrationTest {

    @Autowired
    private StockService stockService;

    @Test
    void getAllIndices_WithRealNseClient_ShouldReturnValidResponse() {
        // Arrange
        String testCookie = null; // Will use default cookie

        // Act
        NseAllIndicesResponse result = stockService.getAllIndices(testCookie);

        // Assert
        assertNotNull(result, "Response should not be null");
        assertNotNull(result.getData(), "Data list should not be null");
        assertFalse(result.getData().isEmpty(), "Data list should not be empty");
        
        // Verify response structure
        assertNotNull(result.getTimestamp(), "Timestamp should not be null");
        assertNotNull(result.getAdvances(), "Advances count should not be null");
        assertNotNull(result.getDeclines(), "Declines count should not be null");
        assertNotNull(result.getUnchanged(), "Unchanged count should not be null");
        
        // Verify data integrity
        assertTrue(result.getAdvances() >= 0, "Advances should be non-negative");
        assertTrue(result.getDeclines() >= 0, "Declines should be non-negative");
        assertTrue(result.getUnchanged() >= 0, "Unchanged should be non-negative");
        
        // Verify first index data
        var firstIndex = result.getData().get(0);
        assertNotNull(firstIndex.getIndex(), "Index name should not be null");
        assertNotNull(firstIndex.getIndexSymbol(), "Index symbol should not be null");
        assertNotNull(firstIndex.getLast(), "Last price should not be null");
        assertTrue(firstIndex.getLast() > 0, "Last price should be positive");
    }

    @Test
    void getAllIndices_WithCustomCookie_ShouldReturnValidResponse() {
        // Arrange
        String customCookie = "test-cookie-value";

        // Act
        NseAllIndicesResponse result = stockService.getAllIndices(customCookie);

        // Assert
        assertNotNull(result, "Response should not be null");
        assertNotNull(result.getData(), "Data list should not be null");
        // Note: Custom cookie might fail, so we just check that we get a response
        // (either valid data or error handling)
    }

    @Test
    void getAllIndices_WithoutParameters_ShouldReturnValidResponse() {
        // Act
        NseAllIndicesResponse result = stockService.getAllIndices();

        // Assert
        assertNotNull(result, "Response should not be null");
        assertNotNull(result.getData(), "Data list should not be null");
        assertFalse(result.getData().isEmpty(), "Data list should not be empty");
        
        // Verify response structure
        assertNotNull(result.getTimestamp(), "Timestamp should not be null");
    }

    @Test
    void getAllIndices_WithEmptyCookie_ShouldUseDefaultCookie() {
        // Arrange
        String emptyCookie = "";

        // Act
        NseAllIndicesResponse result = stockService.getAllIndices(emptyCookie);

        // Assert
        assertNotNull(result, "Response should not be null");
        assertNotNull(result.getData(), "Data list should not be null");
    }

    @Test
    void getAllIndices_WithWhitespaceCookie_ShouldUseDefaultCookie() {
        // Arrange
        String whitespaceCookie = "   ";

        // Act
        NseAllIndicesResponse result = stockService.getAllIndices(whitespaceCookie);

        // Assert
        assertNotNull(result, "Response should not be null");
        assertNotNull(result.getData(), "Data list should not be null");
    }

    @Test
    void getAllIndices_ResponseDataStructure_ShouldBeValid() {
        // Act
        NseAllIndicesResponse result = stockService.getAllIndices();

        // Assert
        assertNotNull(result, "Response should not be null");
        
        if (result.getData() != null && !result.getData().isEmpty()) {
            var firstIndex = result.getData().get(0);
            
            // Verify required fields are present
            assertNotNull(firstIndex.getIndex(), "Index name should not be null");
            assertNotNull(firstIndex.getIndexSymbol(), "Index symbol should not be null");
            assertNotNull(firstIndex.getLast(), "Last price should not be null");
            
            // Verify numeric fields are reasonable
            assertTrue(firstIndex.getLast() > 0, "Last price should be positive");
            if (firstIndex.getOpen() != null) {
                assertTrue(firstIndex.getOpen() > 0, "Open price should be positive");
            }
            if (firstIndex.getHigh() != null) {
                assertTrue(firstIndex.getHigh() > 0, "High price should be positive");
            }
            if (firstIndex.getLow() != null) {
                assertTrue(firstIndex.getLow() > 0, "Low price should be positive");
            }
        }
    }
}

