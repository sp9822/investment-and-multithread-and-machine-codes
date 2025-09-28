package org.example.invest.service;

import org.example.invest.client.NseClient;
import org.example.invest.dto.DateInfo;
import org.example.invest.dto.IndexData;
import org.example.invest.dto.NseAllIndicesResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockServiceTest {
    
    @InjectMocks
    private StockService stockService;

    @Mock
    private NseClient nseClient;

    private NseAllIndicesResponse mockResponse;
    private IndexData mockIndexData;
    private DateInfo mockDateInfo;

    @BeforeEach
    void setUp() {
        // Setup mock DateInfo
        mockDateInfo = new DateInfo();
        mockDateInfo.setPreviousDay("2023-09-27");
        mockDateInfo.setOneWeekAgo("2023-09-20");
        mockDateInfo.setOneMonthAgo("2023-08-28");
        mockDateInfo.setOneYearAgo("2022-09-28");

        // Setup mock IndexData
        mockIndexData = new IndexData();
        mockIndexData.setKey("NIFTY 50");
        mockIndexData.setIndex("NIFTY 50");
        mockIndexData.setIndexSymbol("NIFTY 50");
        mockIndexData.setLast(19500.50);
        mockIndexData.setVariation(150.25);
        mockIndexData.setPercentChange(0.78);
        mockIndexData.setOpen(19400.00);
        mockIndexData.setHigh(19550.75);
        mockIndexData.setLow(19350.25);
        mockIndexData.setPreviousClose(19350.25);
        mockIndexData.setYearHigh(20000.00);
        mockIndexData.setYearLow(18000.00);

        // Setup mock NseAllIndicesResponse
        mockResponse = new NseAllIndicesResponse();
        mockResponse.setData(Arrays.asList(mockIndexData));
        mockResponse.setTimestamp("2023-09-28T09:30:00");
        mockResponse.setAdvances(25);
        mockResponse.setDeclines(20);
        mockResponse.setUnchanged(5);
        mockResponse.setDates(mockDateInfo);
    }

    @Test
    void getAllIndices_WithValidCookie_ShouldReturnResponse() {
        // Arrange
        String testCookie = "test-cookie-value";
        when(nseClient.getAllIndices(testCookie)).thenReturn(mockResponse);

        // Act
        NseAllIndicesResponse result = stockService.getAllIndices(testCookie);

        // Assert
        assertNotNull(result);
        assertEquals(mockResponse.getTimestamp(), result.getTimestamp());
        assertEquals(mockResponse.getAdvances(), result.getAdvances());
        assertEquals(mockResponse.getDeclines(), result.getDeclines());
        assertEquals(mockResponse.getUnchanged(), result.getUnchanged());
        assertNotNull(result.getData());
        assertEquals(1, result.getData().size());
        assertEquals("NIFTY 50", result.getData().get(0).getIndex());
        assertEquals(19500.50, result.getData().get(0).getLast());
        
        verify(nseClient, times(1)).getAllIndices(testCookie);
    }

    @Test
    void getAllIndices_WithNullCookie_ShouldUseDefaultCookie() {
        // Arrange
        when(nseClient.getAllIndices(anyString())).thenReturn(mockResponse);

        // Act
        NseAllIndicesResponse result = stockService.getAllIndices(null);

        // Assert
        assertNotNull(result);
        assertEquals(mockResponse.getTimestamp(), result.getTimestamp());
        verify(nseClient, times(1)).getAllIndices(anyString());
    }

    @Test
    void getAllIndices_WithEmptyCookie_ShouldUseDefaultCookie() {
        // Arrange
        when(nseClient.getAllIndices(anyString())).thenReturn(mockResponse);

        // Act
        NseAllIndicesResponse result = stockService.getAllIndices("");

        // Assert
        assertNotNull(result);
        assertEquals(mockResponse.getTimestamp(), result.getTimestamp());
        verify(nseClient, times(1)).getAllIndices(anyString());
    }

    @Test
    void getAllIndices_WithWhitespaceCookie_ShouldUseDefaultCookie() {
        // Arrange
        when(nseClient.getAllIndices(anyString())).thenReturn(mockResponse);

        // Act
        NseAllIndicesResponse result = stockService.getAllIndices("   ");

        // Assert
        assertNotNull(result);
        assertEquals(mockResponse.getTimestamp(), result.getTimestamp());
        verify(nseClient, times(1)).getAllIndices(anyString());
    }

    @Test
    void getAllIndices_WhenClientThrowsException_ShouldThrowRuntimeException() {
        // Arrange
        String testCookie = "test-cookie-value";
        when(nseClient.getAllIndices(testCookie)).thenThrow(new RuntimeException("NSE API Error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            stockService.getAllIndices(testCookie);
        });

        assertTrue(exception.getMessage().contains("Error fetching NSE indices data"));
        assertTrue(exception.getMessage().contains("NSE API Error"));
        verify(nseClient, times(1)).getAllIndices(testCookie);
    }

    @Test
    void getAllIndices_WithoutCookieParameter_ShouldUseDefaultCookie() {
        // Arrange
        when(nseClient.getAllIndices(anyString())).thenReturn(mockResponse);

        // Act
        NseAllIndicesResponse result = stockService.getAllIndices();

        // Assert
        assertNotNull(result);
        assertEquals(mockResponse.getTimestamp(), result.getTimestamp());
        verify(nseClient, times(1)).getAllIndices(anyString());
    }

    @Test
    void getAllIndices_WithEmptyDataList_ShouldReturnEmptyResponse() {
        // Arrange
        NseAllIndicesResponse emptyResponse = new NseAllIndicesResponse();
        emptyResponse.setData(Arrays.asList());
        emptyResponse.setTimestamp("2023-09-28T09:30:00");
        emptyResponse.setAdvances(0);
        emptyResponse.setDeclines(0);
        emptyResponse.setUnchanged(0);
        emptyResponse.setDates(mockDateInfo);

        String testCookie = "test-cookie-value";
        when(nseClient.getAllIndices(testCookie)).thenReturn(emptyResponse);

        // Act
        NseAllIndicesResponse result = stockService.getAllIndices(testCookie);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getData());
        assertTrue(result.getData().isEmpty());
        assertEquals(0, result.getAdvances());
        assertEquals(0, result.getDeclines());
        assertEquals(0, result.getUnchanged());
        verify(nseClient, times(1)).getAllIndices(testCookie);
    }
}
