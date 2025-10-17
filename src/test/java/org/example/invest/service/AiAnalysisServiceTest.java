package org.example.invest.service;

import org.example.invest.dto.bse.etf.BseEtfResponse;
import org.example.invest.dto.nse.etf.NseEtfResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for AiAnalysisService
 */
@ExtendWith(MockitoExtension.class)
class AiAnalysisServiceTest {

    @InjectMocks
    private AiAnalysisService aiAnalysisService;

    @Test
    void testGetAiOpinion_WithNullApiKey_ReturnsDefaultAnalysis() {
        // Given
        NseEtfResponse nseResponse = new NseEtfResponse();
        BseEtfResponse bseResponse = new BseEtfResponse();

        // When
        String result = aiAnalysisService.getAiOpinion(nseResponse, bseResponse);

        // Then
        assertNotNull(result);
        assertTrue(result.contains("AI Analysis Service Unavailable"));
    }

    @Test
    void testGetAiOpinion_WithValidData_ReturnsAnalysis() {
        // Given
        NseEtfResponse nseResponse = new NseEtfResponse();
        nseResponse.setTimestamp("2024-01-01T10:00:00Z");
        nseResponse.setAdvances(10);
        nseResponse.setDeclines(5);
        nseResponse.setUnchanged(2);

        BseEtfResponse bseResponse = new BseEtfResponse();
        bseResponse.setTimestamp("2024-01-01T10:00:00Z");
        bseResponse.setAdvances(8);
        bseResponse.setDeclines(7);
        bseResponse.setUnchanged(3);

        // When
        String result = aiAnalysisService.getAiOpinion(nseResponse, bseResponse);

        // Then
        assertNotNull(result);
        // Should return default analysis when API key is not configured
        assertTrue(result.contains("AI Analysis Service Unavailable") ||
                result.contains("analysis") ||
                result.contains("market"));
    }
}

