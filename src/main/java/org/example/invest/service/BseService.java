package org.example.invest.service;

import org.example.invest.dto.bse.index.BseMktCapBoardResponse;
import org.example.invest.dto.bse.index.BseRealTimeData;
import org.example.invest.dto.bse.etf.BseEtfResponse;

import java.util.List;

public interface BseService {
    List<BseMktCapBoardResponse> getBseMktCapBoardResponseForAllCategories();

    List<BseRealTimeData> getInvestableIndices(List<BseMktCapBoardResponse> bseMktCapBoardResponseList);

    /**
     * Get all ETF data from BSE
     *
     * @param bseMktCapBoardResponseList
     * @return BSE ETF response containing all ETF data
     */
    BseEtfResponse getAllEtfWithInd(List<BseMktCapBoardResponse> bseMktCapBoardResponseList);

    /**
     * Get investable ETF data from BSE
     * This method filters ETF data to return only investable ETFs
     *
     * @return BSE ETF response containing investable ETF data
     */
    BseEtfResponse getInvestableEtf(BseEtfResponse bseEtfResponse, List<BseRealTimeData> investableIndices);
}
