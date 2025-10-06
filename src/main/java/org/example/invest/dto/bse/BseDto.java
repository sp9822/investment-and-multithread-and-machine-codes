package org.example.invest.dto.bse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.invest.dto.bse.index.BseMktCapBoardResponse;
import org.example.invest.dto.bse.index.BseRealTimeData;
import org.example.invest.dto.bse.etf.BseEtfResponse;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class BseDto {
    private List<BseMktCapBoardResponse> bseMktCapBoardResponseForAllCategories;

    @JsonProperty("investableIndices")
    private List<BseRealTimeData> investableIndices;

    @JsonProperty("allEtf")
    private BseEtfResponse allEtf;

    @JsonProperty("investableEtf")
    private BseEtfResponse investableEtf;
}
