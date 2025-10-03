package org.example.invest.dto.bse.index;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data model for BSE ASON (As On) index data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BseAsOnData {

    @JsonProperty("ScripFlagCode")
    private Integer scripFlagCode;

    @JsonProperty("INDX_CD")
    private String indexCode;

    @JsonProperty("IndexName")
    private String indexName;

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
