package org.example.invest.service;

import org.example.invest.dto.EtfResponse;
import org.example.invest.dto.IndicesResponse;

public interface AlertService {
    EtfResponse sendAlerts();

    void alertEtf(EtfResponse etfResponse);

    void alertIndices(IndicesResponse indicesResponse);
}
