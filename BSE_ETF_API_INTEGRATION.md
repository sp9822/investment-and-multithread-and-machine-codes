# BSE ETF API Integration

This document describes the implementation of BSE ETF data retrieval functionality that follows SOLID principles and proper design patterns.

## Overview

The BSE ETF API integration provides access to ETF (Exchange Traded Fund) data from the Bombay Stock Exchange (BSE) through the endpoint:
```
GET http://localhost:8080/bse/etfs
GET http://localhost:8080/bse/investableEtfs
```

## Architecture & Design Patterns

### 1. SOLID Principles Implementation

#### Single Responsibility Principle (SRP)
- **BseEtfData**: Represents individual ETF data structure
- **BseEtfResponse**: Represents API response wrapper
- **BseClient**: Handles HTTP communication with BSE
- **BseServiceImpl**: Contains business logic for ETF operations
- **BseController**: Handles REST API endpoints

#### Open/Closed Principle (OCP)
- Service interface allows extension without modification
- New ETF data sources can be added by implementing the interface
- HTML parsing logic can be extended for different page formats

#### Liskov Substitution Principle (LSP)
- All implementations can be substituted without breaking functionality
- Service implementations are interchangeable

#### Interface Segregation Principle (ISP)
- Clean, focused interfaces for each component
- No unnecessary dependencies between components

#### Dependency Inversion Principle (DIP)
- High-level modules depend on abstractions (interfaces)
- Dependency injection used throughout the application

### 2. Design Patterns Used

#### Service Layer Pattern
- Business logic separated from controller
- Service interface defines contract
- Implementation handles specific ETF operations

#### Data Transfer Object (DTO) Pattern
- `BseEtfData`: Individual ETF information
- `BseEtfResponse`: API response wrapper with metadata

#### Template Method Pattern
- HTML parsing follows consistent structure
- Extensible parsing methods for different formats

#### Strategy Pattern
- Different parsing strategies for various HTML formats
- Fallback mechanisms for data extraction

## Components

### 1. DTO Classes

#### `BseEtfData.java`
```java
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class BseEtfData {
    @JsonProperty("scripCode")
    private String scripCode;
    
    @JsonProperty("scripName")
    private String scripName;
    
    @JsonProperty("ltp")
    private Double ltp;
    
    // ... additional fields for comprehensive ETF data
}
```

#### `BseEtfResponse.java`
```java
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class BseEtfResponse {
    @JsonProperty("timestamp")
    private String timestamp;
    
    @JsonProperty("data")
    private List<BseEtfData> data;
    
    @JsonProperty("totalCount")
    private Integer totalCount;
    
    // ... additional metadata fields
}
```

### 2. Client Layer (`BseClient.java`)

- **Responsibility**: HTTP communication with BSE ETF market watch page
- **Features**:
  - Proper HTTP headers for BSE API
  - HTML parsing using Jsoup
  - Error handling and logging
  - Fallback parsing strategies

```java
public BseEtfResponse getEtfData() {
    try {
        String url = "https://www.bseindia.com/markets/etf/ETF_MktWatch.aspx";
        HttpHeaders headers = createBseHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);
        
        return parseEtfDataFromHtml(response.getBody());
    } catch (Exception e) {
        log.error("Error fetching BSE ETF data: {}", e.getMessage(), e);
        throw new RuntimeException("Error fetching BSE ETF data: " + e.getMessage(), e);
    }
}
```

### 3. Service Layer

#### Interface (`BseService.java`)
```java
public interface BseService {
    BseEtfResponse getAllEtf();
    BseEtfResponse getInvestableEtf();
}
```

#### Implementation (`BseServiceImpl.java`)
- **Business Logic**: ETF data processing and filtering
- **Investability Criteria**: Filters ETFs based on trading criteria
- **Data Aggregation**: Calculates market statistics

```java
@Override
public BseEtfResponse getInvestableEtf() {
    try {
        BseEtfResponse allEtfResponse = getAllEtf();
        if (allEtfResponse == null || CollectionUtils.isEmpty(allEtfResponse.getData())) {
            return allEtfResponse;
        }

        // Filter ETFs based on investability criteria
        List<BseEtfData> investableEtfs = allEtfResponse.getData().stream()
                .filter(this::isInvestableEtf)
                .collect(Collectors.toList());

        // Create response with filtered data and calculated statistics
        return createFilteredResponse(allEtfResponse, investableEtfs);
    } catch (Exception e) {
        throw new RuntimeException("Error fetching investable BSE ETF data: " + e.getMessage(), e);
    }
}
```

### 4. Controller Layer (`BseController.java`)

- **REST Endpoints**: Clean API endpoints
- **Error Handling**: Proper HTTP status codes
- **Response Format**: Consistent JSON responses

```java
@GetMapping("/etfs")
public ResponseEntity<BseEtfResponse> getAllEtfs(@RequestHeader(value = "Cookie", required = false) String cookie) {
    try {
        BseEtfResponse response = bseServiceImpl.getAllEtf();
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        return ResponseEntity.badRequest().build();
    }
}

@GetMapping("/investableEtfs")
public ResponseEntity<BseEtfResponse> getInvestableEtfs(@RequestHeader(value = "Cookie", required = false) String cookie) {
    try {
        BseEtfResponse response = bseServiceImpl.getInvestableEtf();
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        return ResponseEntity.badRequest().build();
    }
}
```

## API Endpoints

### Get All ETFs
```
GET /bse/etfs
```
- **Description**: Retrieves all ETF data from BSE
- **Response**: `BseEtfResponse` with complete ETF data
- **Headers**: Optional Cookie header

### Get Investable ETFs
```
GET /bse/investableEtfs
```
- **Description**: Retrieves filtered ETF data based on investability criteria
- **Response**: `BseEtfResponse` with filtered ETF data and market statistics
- **Headers**: Optional Cookie header

## Response Format

### Success Response
```json
{
  "timestamp": "2025-10-05T08:59:23.182896Z",
  "data": [
    {
      "scripCode": "532155",
      "scripName": "NIFTY BEES",
      "ltp": 245.50,
      "change": 2.15,
      "changePercent": 0.88,
      "open": 243.35,
      "high": 246.20,
      "low": 242.80,
      "prevClose": 243.35,
      "volume": 1250000,
      "turnover": 306875000.0,
      "week52High": 250.00,
      "week52Low": 200.00,
      "marketCap": 5000000000.0,
      "isActive": true,
      "isSuspended": false,
      "isDelisted": false
    }
  ],
  "totalCount": 1,
  "advances": 1,
  "declines": 0,
  "unchanged": 0,
  "totalTradedValue": 306875000.0,
  "totalTradedVolume": 1250000,
  "totalMarketCap": 5000000000.0,
  "marketStatus": "OPEN",
  "lastUpdated": "2025-10-05T08:59:23.183747Z",
  "source": "BSE",
  "success": true,
  "message": "ETF data retrieved successfully"
}
```

### Error Response
```json
{
  "timestamp": "2025-10-05T08:59:23.182896Z",
  "data": [],
  "totalCount": 0,
  "advances": 0,
  "declines": 0,
  "unchanged": 0,
  "totalTradedValue": 0.0,
  "totalTradedVolume": 0,
  "totalMarketCap": 0.0,
  "marketStatus": "CLOSED",
  "lastUpdated": "2025-10-05T08:59:23.183747Z",
  "source": "BSE",
  "success": false,
  "message": "Error retrieving ETF data",
  "errorCode": "FETCH_ERROR"
}
```

## Investability Criteria

ETFs are considered investable if they meet the following criteria:

1. **Active Status**: ETF must be active and trading
2. **Not Suspended**: ETF must not be suspended from trading
3. **Not Delisted**: ETF must not be delisted
4. **Valid Price**: Last traded price must be greater than 0
5. **Trading Volume**: Must have trading volume > 0
6. **Turnover**: Must have turnover > 0
7. **Price Range**: 52-week high and low must be valid and high > low
8. **Market Cap**: Minimum market capitalization of 1M

## Error Handling

### Client Layer
- HTTP connection errors
- HTML parsing errors
- Data format validation errors

### Service Layer
- Business logic validation
- Data filtering errors
- Statistics calculation errors

### Controller Layer
- Input validation
- Service layer exceptions
- HTTP response formatting

## Dependencies

### Required Dependencies
- **Spring Boot**: Web framework
- **Jsoup**: HTML parsing library
- **Lombok**: Code generation
- **Jackson**: JSON serialization
- **Apache Commons**: Utility functions

### Maven Dependency
```xml
<dependency>
    <groupId>org.jsoup</groupId>
    <artifactId>jsoup</artifactId>
    <version>1.16.1</version>
</dependency>
```

## Testing

### Unit Tests
- Service layer business logic
- HTML parsing functionality
- Data filtering criteria
- Error handling scenarios

### Integration Tests
- End-to-end API testing
- HTTP client functionality
- Response format validation

### Manual Testing
```bash
# Test all ETFs endpoint
curl -X GET "http://localhost:8080/bse/etfs" -H "Content-Type: application/json"

# Test investable ETFs endpoint
curl -X GET "http://localhost:8080/bse/investableEtfs" -H "Content-Type: application/json"
```

## Future Enhancements

### 1. Enhanced HTML Parsing
- Dynamic selector detection
- Multiple page format support
- Real-time data extraction

### 2. Caching
- Redis integration for data caching
- TTL-based cache invalidation
- Performance optimization

### 3. Monitoring
- Metrics collection
- Health checks
- Performance monitoring

### 4. Configuration
- Externalized configuration
- Environment-specific settings
- Feature flags

## Security Considerations

### 1. Rate Limiting
- API rate limiting
- Request throttling
- Abuse prevention

### 2. Data Validation
- Input sanitization
- Output validation
- XSS prevention

### 3. Error Handling
- Secure error messages
- Logging best practices
- Information disclosure prevention

## Performance Considerations

### 1. Async Processing
- Asynchronous data fetching
- Non-blocking operations
- Thread pool management

### 2. Connection Pooling
- HTTP connection reuse
- Connection pool configuration
- Timeout management

### 3. Memory Management
- Efficient data structures
- Garbage collection optimization
- Memory leak prevention

This implementation provides a robust, scalable, and maintainable solution for BSE ETF data retrieval while adhering to SOLID principles and industry best practices.

