package org.example.invest.dto.bse.index;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data model for BSE Index Fundamentals API response
 * Maps the fundamental data for BSE indices including market cap, ratios, and trading statistics
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class BseIndexFundamentals {

    /**
     * Full market capitalization in crores
     */
    @JsonProperty("Full")
    private String fullMarketCap;

    /**
     * Free float market capitalization in crores
     */
    @JsonProperty("FreeFloat")
    private String freeFloatMarketCap;

    /**
     * Price-to-Earnings ratio
     */
    @JsonProperty("PE")
    private Double peRatio;

    /**
     * Price-to-Book ratio
     */
    @JsonProperty("PB")
    private Double pbRatio;

    /**
     * Dividend yield percentage
     */
    @JsonProperty("DivYield")
    private Double dividendYield;

    /**
     * Number of advancing scrips
     */
    @JsonProperty("AdvScrp")
    private String advancingScrips;

    /**
     * Turnover percentage for advancing scrips
     */
    @JsonProperty("AdvTO")
    private String advancingTurnover;

    /**
     * Number of declining scrips
     */
    @JsonProperty("DecScrp")
    private String decliningScrips;

    /**
     * Turnover percentage for declining scrips
     */
    @JsonProperty("DecTO")
    private String decliningTurnover;

    /**
     * Number of unchanged scrips
     */
    @JsonProperty("UnchgScrp")
    private String unchangedScrips;

    /**
     * Turnover percentage for unchanged scrips
     */
    @JsonProperty("UnchgTO")
    private String unchangedTurnover;

    /**
     * Number of non-traded scrips
     */
    @JsonProperty("NonTrdScrp")
    private String nonTradedScrips;

    /**
     * Turnover percentage for non-traded scrips
     */
    @JsonProperty("NonTrdTO")
    private String nonTradedTurnover;

    /**
     * Total number of scrips
     */
    @JsonProperty("TotalScrp")
    private String totalScrips;

    /**
     * Total turnover percentage
     */
    @JsonProperty("TotalTO")
    private String totalTurnover;

    /**
     * Date of the data in DD MMM YYYY format
     */
    @JsonProperty("Dt")
    private String date;
}
