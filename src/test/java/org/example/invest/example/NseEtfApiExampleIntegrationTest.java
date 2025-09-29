package org.example.invest.example;

import org.example.invest.dto.NseEtfResponse;
import org.example.invest.service.StockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Integration test for ETF API example
 */
@SpringBootTest
@ActiveProfiles("test")
class NseEtfApiExampleIntegrationTest {

    @Autowired
    private StockService stockService;

    @Autowired
    private NseEtfApiExample etfApiExample;

    @Test
    void contextLoads() {
        assertNotNull(stockService);
        assertNotNull(etfApiExample);
    }

    @Test
    void etfApiExample_ShouldNotThrowException() {
        // This test verifies that the ETF API example can be instantiated
        // and the methods exist without throwing compilation errors
        assertDoesNotThrow(() -> {
            // Test that the example class can be instantiated and methods exist
            assertNotNull(etfApiExample);
        });
    }
}

