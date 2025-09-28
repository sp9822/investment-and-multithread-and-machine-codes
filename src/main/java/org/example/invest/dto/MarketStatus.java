package org.example.invest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * DTO for market status in NSE ETF API response
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class MarketStatus {
    
    @JsonProperty("market")
    private String market;
    
    @JsonProperty("marketStatus")
    private String marketStatus;
    
    @JsonProperty("tradeDate")
    private String tradeDate;
    
    @JsonProperty("index")
    private String index;
    
    @JsonProperty("last")
    private Double last;
    
    @JsonProperty("variation")
    private Double variation;
    
    @JsonProperty("percentChange")
    private Double percentChange;
    
    @JsonProperty("marketStatusMessage")
    private String marketStatusMessage;
}
