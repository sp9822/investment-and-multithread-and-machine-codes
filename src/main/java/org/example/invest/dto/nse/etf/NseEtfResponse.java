package org.example.invest.dto.nse.etf;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.invest.dto.MarketStatus;

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
