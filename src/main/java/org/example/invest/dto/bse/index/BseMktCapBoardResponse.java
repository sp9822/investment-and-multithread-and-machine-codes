package org.example.invest.dto.bse.index;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Main response model for BSE Market Cap Board API
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BseMktCapBoardResponse {

    @JsonProperty("RealTime")
    private List<BseRealTimeData> realTime;

    @JsonProperty("ASON")
    private List<BseAsOnData> asOn;

    @JsonProperty("EOD")
    private List<BseEodData> eod;
}
