package org.example.invest.dto.bse.etf;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CurrRate {
    @JsonProperty("LTP")
    private String ltp;

    @JsonProperty("Chg")
    private String chg;

    @JsonProperty("PcChg")
    private String pcChg;

    @JsonProperty("D_Cpricelink")
    private String dCpricelink;

    @JsonProperty("IssueChgVal")
    private String issueChgVal;

    @JsonProperty("IssueChgPC")
    private String issueChgPC;
}
