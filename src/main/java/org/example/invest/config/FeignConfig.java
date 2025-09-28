package org.example.invest.config;

import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import java.util.concurrent.TimeUnit;

/**
 * Feign configuration class for Java 11
 * This class provides custom configuration for Feign clients
 */
@Configuration
public class FeignConfig {
    
    /**
     * Configure Feign logging level
     * @return Logger.Level for Feign requests/responses
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL; // Logs request and response headers and body
    }
    
    /**
     * Configure request options for Feign clients
     * @return Request.Options with custom timeout settings
     */
    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(
            30, TimeUnit.SECONDS,  // Connect timeout (increased for NSE API)
            60, TimeUnit.SECONDS, // Read timeout
            true                  // Follow redirects
        );
    }
    
    /**
     * Request interceptor to handle compression properly
     * @return RequestInterceptor that manages Accept-Encoding headers
     */
    @Bean
    public RequestInterceptor compressionInterceptor() {
        return template -> {
            // Remove Brotli from Accept-Encoding to avoid decompression issues
            template.header(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate");
        };
    }

    /**
     * Custom error decoder for handling HTTP errors
     * @return Custom ErrorDecoder implementation
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }
    
    /**
     * Custom error decoder implementation
     */
    public static class CustomErrorDecoder implements ErrorDecoder {
        private final ErrorDecoder defaultErrorDecoder = new Default();
        
        @Override
        public Exception decode(String methodKey, feign.Response response) {
            switch (response.status()) {
                case 400:
                    return new IllegalArgumentException("Bad Request: " + response.reason());
                case 401:
                    return new SecurityException("Unauthorized: " + response.reason());
                case 403:
                    return new SecurityException("Forbidden: " + response.reason());
                case 404:
                    return new IllegalArgumentException("Not Found: " + response.reason());
                case 500:
                    return new RuntimeException("Internal Server Error: " + response.reason());
                default:
                    return defaultErrorDecoder.decode(methodKey, response);
            }
        }
    }
}
