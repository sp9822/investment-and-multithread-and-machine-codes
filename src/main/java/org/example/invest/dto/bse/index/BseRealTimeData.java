package org.example.invest.dto.bse.index;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.example.invest.dto.IndexData;

import java.time.LocalDateTime;

/**
 * Data model for BSE RealTime index data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BseRealTimeData extends IndexData {

    @JsonProperty("ScripFlagCode")
    private Integer scripFlagCode;

    @JsonProperty("INDX_CD")
    private String indexCode;

    @JsonProperty("Processed_INDX_CD")
    private String processedIndexSymbol;

    @JsonProperty("IndexName")
    private String indexName;

    @JsonProperty("Processed_IndexName")
    private String processedIndex;

    @JsonProperty("yearHighToLatestDiffPer")
    private Double yearHighToLatestDiffPer;

    @JsonProperty("latestToYearLowDiffPer")
    private Double latestToYearLowDiffPer;

    @JsonProperty("yearHighToYearLowDiffPer")
    private Double yearHighToYearLowDiffPer;

    @JsonProperty("I_open")
    private Double open;

    @JsonProperty("High")
    private Double high;

    @JsonProperty("Low")
    private Double low;

    @JsonProperty("Curvalue")
    private Double currentValue;

    @JsonProperty("Prev_Close")
    private Double previousClose;

    @JsonProperty("Chg")
    private Double change;

    @JsonProperty("ChgPer")
    private Double changePercentage;

    @JsonProperty("Week52High")
    private Double week52High;

    @JsonProperty("Week52Low")
    private Double week52Low;

    @JsonProperty("MKTCAP")
    private Double marketCap;

    @JsonProperty("MktcapPerc")
    private Double marketCapPercentage;

    @JsonProperty("NET_TURN")
    private Double netTurnover;

    @JsonProperty("TurnoverPerc")
    private Double turnoverPercentage;

    @JsonProperty("DT_TM")
    private LocalDateTime dateTime;

    @JsonProperty("WebURL")
    private String webUrl;
}
