# BSE API Integration

This document describes the BSE (Bombay Stock Exchange) API integration that has been implemented to retrieve market cap board data.

## Overview

The BSE API integration provides access to market cap board data through the endpoint:
```
https://api.bseindia.com/BseIndiaAPI/api/MktCapBoard_indstream/w?cat={category}&type={type}
```

## Components

### 1. BSE Client (`BseClient.java`)
- Feign client interface for making HTTP calls to BSE API
- Supports categories 1-5 with configurable type parameter
- Includes proper headers including Referer for BSE API

### 2. Data Models

#### `BseRealTimeData.java`
- Represents real-time index data
- Fields include: index code, name, open, high, low, current value, change, market cap, etc.

#### `BseAsOnData.java`
- Represents ASON (As On) index data
- Similar structure to real-time data

#### `BseEodData.java`
- Represents EOD (End of Day) index data
- Fields include: indices watch name, index code, current value, previous day close, change, etc.

#### `BseMktCapBoardResponse.java`
- Main response wrapper containing all three data types
- Fields: RealTime, ASON, EOD

### 3. Configuration (`BseApiConfig.java`)
- Feign client configuration
- Request interceptors for common headers
- Timeout configurations
- Logging configuration

### 4. Service Layer (`BseService.java`)
- Business logic for BSE API operations
- Methods to fetch data for single/multiple categories
- Data filtering and processing
- Error handling and validation

### 5. Controller (`BseController.java`)
- REST endpoints for BSE API operations
- Supports various data retrieval patterns
- Error handling and response formatting

### 6. Example Usage (`BseApiExample.java`)
- Demonstrates various usage patterns
- Error handling examples
- Data processing examples

## API Endpoints

### Get Market Cap Board Data for Specific Category
```
GET /api/bse/mkt-cap-board/{category}
```
- `category`: 1-5

### Get Market Cap Board Data with Custom Type
```
GET /api/bse/mkt-cap-board/{category}/{type}
```
- `category`: 1-5
- `type`: Type parameter (default: 2)

### Get All Categories Data
```
GET /api/bse/mkt-cap-board/all
```
- Returns data for all categories (1-5)

### Get Real-Time Data Only
```
GET /api/bse/real-time/all
```
- Returns only real-time data for all categories

### Get ASON Data Only
```
GET /api/bse/as-on/all
```
- Returns only ASON data for all categories

### Get EOD Data Only
```
GET /api/bse/eod/all
```
- Returns only EOD data for all categories

### Get Data for Specific Categories
```
GET /api/bse/mkt-cap-board/categories?categories=1,3,5
```
- `categories`: Comma-separated list of category numbers

## Usage Examples

### Service Layer Usage
```java
@Autowired
private BseService bseService;

// Get data for category 1
BseMktCapBoardResponse response = bseService.getMktCapBoardData(1);

// Get data for all categories
Map<Integer, BseMktCapBoardResponse> allData = bseService.getAllCategoriesData();

// Get only real-time data
Map<Integer, List<BseRealTimeData>> realTimeData = bseService.getAllRealTimeData();
```

### Controller Usage
```bash
# Get data for category 1
curl http://localhost:8080/api/bse/mkt-cap-board/1

# Get data for all categories
curl http://localhost:8080/api/bse/mkt-cap-board/all

# Get real-time data only
curl http://localhost:8080/api/bse/real-time/all
```

## Error Handling

The integration includes comprehensive error handling:
- Category validation (1-5)
- API call failures
- Data processing errors
- Network timeouts

## Configuration

The BSE API client is configured with:
- 10-second connect timeout
- 30-second read timeout
- Proper headers for BSE API compatibility
- Referer header set to `https://www.bseindia.com/`

## Dependencies

Required dependencies (already included in pom.xml):
- Spring Cloud OpenFeign
- Jackson for JSON processing
- Lombok for boilerplate code reduction

## Notes

- The BSE API requires a Referer header for proper functionality
- Categories 1-5 represent different market cap segments
- The API returns three types of data: RealTime, ASON, and EOD
- All timestamps are in ISO format
- Market cap values are in crores
- Change percentages are calculated values
