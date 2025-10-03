package org.example.invest.service;

import org.example.invest.dto.bse.index.BseMktCapBoardResponse;
import org.example.invest.dto.bse.index.BseRealTimeData;

import java.util.List;

public interface BseService {
    List<BseMktCapBoardResponse> getBseMktCapBoardResponseForAllCategories();

    List<BseRealTimeData> getInvestableIndices(List<BseMktCapBoardResponse> bseMktCapBoardResponseList);
}
