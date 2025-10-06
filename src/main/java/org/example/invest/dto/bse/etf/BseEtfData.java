package org.example.invest.dto.bse.etf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.invest.dto.bse.index.BseRealTimeData;
import org.example.invest.dto.nse.index.NseIndexData;

/**
 * DTO for individual BSE ETF data
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class BseEtfData {

    @JsonProperty("scripCode")
    private String scripCode;

    @JsonProperty("processedScripCode")
    private String processedScripCode;

    @JsonProperty("scripName")
    private String scripName;

    @JsonProperty("processedScripName")
    private String processedScripName;

    @JsonProperty("navToMarketLtPDelta")
    private Double navToMarketLtPDelta;

    @JsonProperty("navToMarketLtPDeltaPercent")
    private Double navToMarketLtPDeltaPercent;

    @JsonProperty("nav")
    private Double nav;

    @JsonProperty("navDate")
    private String navDate;

    @JsonProperty("ltp")
    private Double ltp;

    @JsonProperty("yearHighToLatestDiffPer")
    private Double yearHighToLatestDiffPer;

    @JsonProperty("latestToYearLowDiffPer")
    private Double latestToYearLowDiffPer;

    @JsonProperty("yearHighToYearLowDiffPer")
    private Double yearHighToYearLowDiffPer;

    @JsonProperty("change")
    private Double change;

    @JsonProperty("changePercent")
    private Double changePercent;

    @JsonProperty("open")
    private Double open;

    @JsonProperty("high")
    private Double high;

    @JsonProperty("low")
    private Double low;

    @JsonProperty("prevClose")
    private Double prevClose;

    @JsonProperty("volume")
    private Long volume;

    @JsonProperty("turnover")
    private Double turnover;

    @JsonProperty("week52High")
    private Double week52High;

    @JsonProperty("week52Low")
    private Double week52Low;

    @JsonProperty("marketCap")
    private Double marketCap;

    @JsonProperty("peRatio")
    private Double peRatio;

    @JsonProperty("pbRatio")
    private Double pbRatio;

    @JsonProperty("dividendYield")
    private Double dividendYield;

    @JsonProperty("isin")
    private String isin;

    @JsonProperty("sector")
    private String sector;

    @JsonProperty("assetClass")
    private String assetClass;

    @JsonProperty("expenseRatio")
    private Double expenseRatio;

    @JsonProperty("aum")
    private Double aum; // Assets Under Management

    @JsonProperty("lastTradingDate")
    private String lastTradingDate;

    @JsonProperty("listingDate")
    private String listingDate;

    @JsonProperty("faceValue")
    private Double faceValue;

    @JsonProperty("bookValue")
    private Double bookValue;

    @JsonProperty("eps")
    private Double eps; // Earnings Per Share

    @JsonProperty("ceilingPrice")
    private Double ceilingPrice;

    @JsonProperty("floorPrice")
    private Double floorPrice;

    @JsonProperty("wtAvgPrice")
    private Double wtAvgPrice;

    @JsonProperty("Circuit")
    private Double circuit;

    @JsonProperty("Limits")
    private Double limits;

    @JsonProperty("totalTradedValue")
    private Double totalTradedValue;

    @JsonProperty("totalTradedVolume")
    private Long totalTradedVolume;

    @JsonProperty("noOfTrades")
    private Long noOfTrades;

    @JsonProperty("deliverableQuantity")
    private Long deliverableQuantity;

    @JsonProperty("deliveryPercentage")
    private Double deliveryPercentage;

    @JsonProperty("isActive")
    private Boolean isActive;

    @JsonProperty("isSuspended")
    private Boolean isSuspended;

    @JsonProperty("isDelisted")
    private Boolean isDelisted;

    @JsonProperty("bseIndexData")
    private BseRealTimeData bseIndexData;

    @JsonProperty("nseIndexData")
    private NseIndexData nseIndexData;
}
