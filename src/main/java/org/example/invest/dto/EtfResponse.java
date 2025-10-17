package org.example.invest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.invest.dto.bse.etf.BseEtfResponse;
import org.example.invest.dto.nse.etf.NseEtfResponse;


@Data
@AllArgsConstructor
public class EtfResponse {
    private NseEtfResponse nse;
    private BseEtfResponse bse;
    private Object aiOpinion;
}
