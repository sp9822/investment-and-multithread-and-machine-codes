# @EnableFeignClients in Java 11

This project demonstrates how to use Spring Cloud OpenFeign with `@EnableFeignClients` annotation in Java 11.

## Setup

### 1. Dependencies

The following dependencies have been added to `pom.xml`:

```xml
<!-- Spring Cloud OpenFeign -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

### 2. Spring Cloud BOM

Added Spring Cloud BOM for dependency management:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### 3. Enable Feign Clients

In your main application class (`Main.java`):

```java
@SpringBootApplication
@EnableFeignClients(basePackages = "org.example")
public class Main {
    // ...
}
```

## Usage Examples

### 1. Feign Client Interface

```java
@FeignClient(name = "nse-client", url = "https://www.nseindia.com/api")
public interface NseClient {
    
    @GetMapping("/quote-equity")
    String getStockQuote(@RequestParam("symbol") String symbol);
    
    @GetMapping("/marketStatus")
    String getMarketStatus();
    
    @GetMapping("/company/{symbol}")
    String getCompanyInfo(@PathVariable("symbol") String symbol);
}
```

### 2. Service Layer

```java
@Service
public class StockService {
    
    @Autowired
    private NseClient nseClient;
    
    public String getStockQuote(String symbol) {
        return nseClient.getStockQuote(symbol);
    }
}
```

### 3. REST Controller

```java
@RestController
@RequestMapping("/api/stocks")
public class StockController {
    
    @Autowired
    private StockService stockService;
    
    @GetMapping("/quote/{symbol}")
    public String getStockQuote(@PathVariable String symbol) {
        return stockService.getStockQuote(symbol);
    }
}
```

## Configuration

### Custom Feign Configuration

```java
@Configuration
public class FeignConfig {
    
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
    
    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(
            10, TimeUnit.SECONDS,  // Connect timeout
            60, TimeUnit.SECONDS, // Read timeout
            true                  // Follow redirects
        );
    }
}
```

## Key Features

1. **Declarative HTTP Client**: Define HTTP clients using simple interfaces
2. **Spring Integration**: Seamless integration with Spring Boot
3. **Load Balancing**: Built-in support for load balancing (with Ribbon/Eureka)
4. **Circuit Breaker**: Integration with Hystrix for fault tolerance
5. **Custom Configuration**: Flexible configuration options
6. **Error Handling**: Custom error decoders for better error handling

## Testing the API

Once the application is running, you can test the endpoints:

```bash
# Get all NSE indices (with custom cookie)
curl -H "Cookie: your-cookie-value" http://localhost:8080/api/stocks/indices

# Get all NSE indices (using default cookie)
curl http://localhost:8080/api/stocks/indices/default

# Get stock quote
curl http://localhost:8080/api/stocks/quote/RELIANCE

# Get market status
curl http://localhost:8080/api/stocks/market-status

# Get company info
curl http://localhost:8080/api/stocks/company/TCS
```

## NSE AllIndices API Integration

The application now includes integration with the NSE allIndices API:

### Endpoints:
- `GET /api/stocks/indices` - Get all NSE indices with custom cookie
- `GET /api/stocks/indices/default` - Get all NSE indices with default cookie

### Response Structure:
The API returns comprehensive index data including:
- Index name and symbol
- Current price, open, high, low
- Percentage changes (1D, 30D, 365D)
- PE, PB, DY ratios
- Chart URLs for different time periods
- Market statistics (advances, declines, unchanged)

### Cookie Management:
- The application includes a `NseCookieUtil` class for cookie management
- Default cookie is provided for testing purposes
- In production, obtain fresh cookies from NSE website

## Java 11 Compatibility

This setup is fully compatible with Java 11 and uses:
- Spring Boot 2.7.4
- Spring Cloud 2021.0.8
- Java 11 features and syntax
