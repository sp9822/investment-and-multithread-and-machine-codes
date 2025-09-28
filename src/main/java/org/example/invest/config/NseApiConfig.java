package org.example.invest.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for NSE API headers
 * This class handles the required headers for NSE API calls
 */
@Configuration
public class NseApiConfig {
    
    /**
     * Request interceptor to add required headers for NSE API
     * @return RequestInterceptor that adds NSE-specific headers
     */
    @Bean
    public RequestInterceptor nseRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // Add User-Agent header
                template.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
                
                // Add Accept header
                template.header("Accept", "application/json, text/plain, */*");
                
                // Add Accept-Language header
                template.header("Accept-Language", "en-US,en;q=0.9");
                
                // Add Accept-Encoding header (removed br to avoid Brotli decompression issues)
                template.header("Accept-Encoding", "gzip, deflate");
                
                // Add Connection header
                template.header("Connection", "keep-alive");
                
                // Add Referer header
                template.header("Referer", "https://www.nseindia.com/");
                
                // Add Origin header
                template.header("Origin", "https://www.nseindia.com");
                
                // Add Sec-Fetch-Dest header
                template.header("Sec-Fetch-Dest", "empty");
                
                // Add Sec-Fetch-Mode header
                template.header("Sec-Fetch-Mode", "cors");
                
                // Add Sec-Fetch-Site header
                template.header("Sec-Fetch-Site", "same-origin");
                
                // Add Sec-Ch-Ua header
                template.header("Sec-Ch-Ua", "\"Chromium\";v=\"91\", \" Not;A Brand\";v=\"99\"");
                
                // Add Sec-Ch-Ua-Mobile header
                template.header("Sec-Ch-Ua-Mobile", "?0");
                
                // Add Sec-Ch-Ua-Platform header
                template.header("Sec-Ch-Ua-Platform", "\"Windows\"");
            }
        };
    }
}

