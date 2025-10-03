package org.example.invest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.invest.dto.bse.index.BseMktCapBoardResponse;
import org.example.invest.dto.bse.index.BseRealTimeData;

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

    //allEtf;
    //investableEtf;
}
