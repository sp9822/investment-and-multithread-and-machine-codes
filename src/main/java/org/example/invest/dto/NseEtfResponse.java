package org.example.invest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * DTO for NSE ETF API response
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class NseEtfResponse {

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("data")
    private List<EtfData> data;

    @JsonProperty("advances")
    private Integer advances;

    @JsonProperty("declines")
    private Integer declines;

    @JsonProperty("unchanged")
    private Integer unchanged;

    @JsonProperty("navDate")
    private String navDate;

    @JsonProperty("totalTradedValue")
    private Double totalTradedValue;

    @JsonProperty("totalTradedVolume")
    private Long totalTradedVolume;

    @JsonProperty("marketStatus")
    private MarketStatus marketStatus;
}
