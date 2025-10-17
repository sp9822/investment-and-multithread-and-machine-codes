package org.example.invest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.invest.dto.bse.index.BseRealTimeData;
import org.example.invest.dto.nse.index.NseAllIndicesResponse;

import java.util.List;

@Data
@AllArgsConstructor
public class IndicesResponse {
    private NseAllIndicesResponse nse;
    private List<BseRealTimeData> bse;
    private Object aiOpinion;
}
