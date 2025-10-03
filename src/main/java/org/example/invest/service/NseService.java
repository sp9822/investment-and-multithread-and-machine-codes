package org.example.invest.service;

import org.example.invest.dto.nse.etf.NseEtfResponse;
import org.example.invest.dto.nse.index.NseAllIndicesResponse;

public interface NseService {
    NseAllIndicesResponse getAllIndices();

    NseEtfResponse getAllEtfWithInd(String nseIndiaCookie, NseAllIndicesResponse nseAllIndicesResponse);
}
