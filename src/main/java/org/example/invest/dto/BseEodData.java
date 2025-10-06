package org.example.invest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data model for BSE EOD (End of Day) index data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BseEodData {

    @JsonProperty("IndicesWatchName")
    private String indicesWatchName;

    @JsonProperty("INDEX_CODE")
    private String indexCode;

    @JsonProperty("Curvalue")
    private Double currentValue;

    @JsonProperty("PrevDayClose")
    private Double previousDayClose;

    @JsonProperty("CHNG")
    private Double change;

    @JsonProperty("CHNGPER")
    private Double changePercentage;

    @JsonProperty("DT_TM")
    private LocalDateTime dateTime;

    @JsonProperty("WebURL")
    private String webUrl;

    @JsonProperty("IndexSrNo")
    private Double indexSerialNumber;

    @JsonProperty("rn")
    private Integer rowNumber;
}
