# NSE ETF API Integration

This document describes the integration of the NSE ETF API into the existing NseClient class.

## Overview

The ETF API integration allows you to fetch ETF (Exchange Traded Fund) data from the National Stock Exchange (NSE) of India. The API provides comprehensive information about various ETFs including their current prices, NAV, trading volumes, and metadata.

## API Endpoint

- **URL**: `https://www.nseindia.com/api/etf`
- **Method**: GET
- **Authentication**: Requires Cookie header

## Integration Components

### 1. DTO Classes

The following DTO classes have been created to handle the ETF API response:

- `NseEtfResponse` - Main response wrapper
- `EtfData` - Individual ETF data
- `EtfMeta` - ETF metadata
- `QuotePreOpenStatus` - Quote pre-open status information
- `MarketStatus` - Market status information

### 2. NseClient Interface

Added the following method to the `NseClient` interface:

```java
@GetMapping("/etf")
NseEtfResponse getEtfData(@RequestHeader("Cookie") String cookie);
```

### 3. StockService

Added ETF methods to the `StockService` class:

```java
// Get ETF data with custom cookie
public NseEtfResponse getEtfData(String cookie)

// Get ETF data with default cookie
public NseEtfResponse getEtfData()
```

### 4. REST Controller

Added ETF endpoints to the `StockController`:

```java
// Get ETF data with custom cookie
@GetMapping("/etf")
public NseEtfResponse getEtfData(@RequestHeader(value = "Cookie", required = false) String cookie)

// Get ETF data with default cookie
@GetMapping("/etf/default")
public NseEtfResponse getEtfDataDefault()
```

## Usage Examples

### 1. Using StockService Directly

```java
@Autowired
private StockService stockService;

// Get ETF data with custom cookie
String cookie = "your-cookie-value";
NseEtfResponse response = stockService.getEtfData(cookie);

// Get ETF data with default cookie
NseEtfResponse response = stockService.getEtfData();
```

### 2. Using REST API

```bash
# Get ETF data with custom cookie
curl -H "Cookie: your-cookie-value" http://localhost:8080/api/stocks/etf

# Get ETF data with default cookie
curl http://localhost:8080/api/stocks/etf/default
```

### 3. Using Example Class

```java
@Autowired
private NseEtfApiExample etfApiExample;

// Demonstrate ETF API usage
etfApiExample.demonstrateEtfApiUsage();

// Demonstrate ETF API usage with custom cookie
etfApiExample.demonstrateEtfApiUsageWithCookie("your-cookie-value");
```

## Response Structure

The ETF API returns the following structure:

```json
{
    "timestamp": "26-Sep-2025 16:00:00",
    "data": [
        {
            "symbol": "MONQ50",
            "assets": "Nasdaq Q-50 Total Return Index",
            "open": "90.38",
            "high": "92.89",
            "low": "89.5",
            "ltP": "92.8",
            "chn": "2.83",
            "per": "3.15",
            "qty": "318329",
            "trdVal": "29133470.08",
            "nav": "84.4162",
            "wkhi": "94.89",
            "wklo": "61.31",
            "prevClose": "89.97",
            "perChange365d": 45.05,
            "perChange30d": 13.93,
            "meta": {
                "symbol": "MONQ50",
                "companyName": "Motilal Oswal Mutual Fund - Motilal Oswal Nasdaq Q 50 ETF",
                "industry": "Mutual Fund Scheme",
                "isin": "INF247L01AU3",
                "listingDate": "2021-12-29"
            }
        }
    ],
    "advances": 55,
    "declines": 212,
    "unchanged": 7,
    "navDate": "25-Sep-2025",
    "totalTradedValue": 30899660190.52,
    "totalTradedVolume": 334557840,
    "marketStatus": {
        "market": "Capital Market",
        "marketStatus": "Close",
        "tradeDate": "26-Sep-2025 15:30",
        "index": "NIFTY 50",
        "last": 24654.7,
        "variation": -236.15,
        "percentChange": -0.95
    }
}
```

## Key Fields

- **symbol**: ETF symbol (e.g., "MONQ50")
- **assets**: Underlying asset description
- **ltP**: Last traded price
- **nav**: Net Asset Value
- **perChange365d**: Percentage change in 365 days
- **perChange30d**: Percentage change in 30 days
- **meta**: ETF metadata including company name, ISIN, listing date
- **marketStatus**: Current market status and NIFTY 50 information

## Error Handling

The integration includes proper error handling:

- Network errors are wrapped in `RuntimeException` with descriptive messages
- Cookie validation is handled by the existing `NseCookieUtil`
- All methods include try-catch blocks for robust error handling

## Testing

The integration includes comprehensive unit tests:

- `StockServiceEtfTest` - Unit tests for ETF service methods
- `NseEtfApiExampleIntegrationTest` - Integration tests for the example class

## Dependencies

The integration uses existing dependencies:

- Spring Boot
- Spring Cloud OpenFeign
- Jackson for JSON processing
- JUnit 5 for testing

## Notes

- The ETF API requires proper cookie authentication
- The API returns data in real-time during market hours
- All price values are returned as strings to preserve precision
- The integration follows the same patterns as existing NSE API integrations

