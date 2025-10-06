package org.example.invest.dto.bse.etf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * DTO for BSE ETF API response
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class BseEtfResponse {

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("data")
    private List<BseEtfData> data;

    @JsonProperty("totalCount")
    private Integer totalCount;

    @JsonProperty("advances")
    private Integer advances;

    @JsonProperty("declines")
    private Integer declines;

    @JsonProperty("unchanged")
    private Integer unchanged;

    @JsonProperty("totalTradedValue")
    private Double totalTradedValue;

    @JsonProperty("totalTradedVolume")
    private Long totalTradedVolume;

    @JsonProperty("totalMarketCap")
    private Double totalMarketCap;

    @JsonProperty("marketStatus")
    private String marketStatus;

    @JsonProperty("lastUpdated")
    private String lastUpdated;

    @JsonProperty("source")
    private String source;

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("message")
    private String message;

    @JsonProperty("errorCode")
    private String errorCode;
}
