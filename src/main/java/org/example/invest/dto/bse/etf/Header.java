package org.example.invest.dto.bse.etf;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Header {
    @JsonProperty("Noticecnt")
    private String noticecnt;

    @JsonProperty("PrevClose")
    private String prevClose;

    @JsonProperty("Open")
    private String open;

    @JsonProperty("High")
    private String high;

    @JsonProperty("Low")
    private String low;

    @JsonProperty("LTP")
    private String ltp;

    @JsonProperty("DisplayText")
    private String displayText;

    @JsonProperty("Category")
    private String category;

    @JsonProperty("PRE_OPEN_NO_BIDS")
    private String preOpenNoBids;

    @JsonProperty("PRE_OPEN_NO_I_PRICE")
    private String preOpenNoIPrice;

    @JsonProperty("PRE_OPEN_I_PRICE")
    private String preOpenIPrice;

    @JsonProperty("PRE_OPEN_I_PRICE_QTY")
    private String preOpenIPriceQty;

    @JsonProperty("PCAS_NO_BIDS")
    private String pcasNoBids;

    @JsonProperty("PCAS_INDICATIVE_PRICE")
    private String pcasIndicativePrice;

    @JsonProperty("PCAS_INDICATIVE_QTY")
    private String pcasIndicativeQty;

    @JsonProperty("PERODIC_CALL_AUCTION")
    private String periodicCallAuction;

    @JsonProperty("GSMURL")
    private String gsmUrl;

    @JsonProperty("GSMText")
    private String gsmText;

    @JsonProperty("Invit")
    private String invit;

    @JsonProperty("Ason")
    private String ason;

    @JsonProperty("NAVRate")
    private Double navRate;

    @JsonProperty("NAVdttm")
    private String navDttm;

    @JsonProperty("ASMText")
    private String asmText;

    @JsonProperty("SMSText")
    private String smsText;

    @JsonProperty("IRPText")
    private String irpText;

    @JsonProperty("ASMURL")
    private String asmUrl;

    @JsonProperty("SMSURL")
    private String smsUrl;

    @JsonProperty("IRPURL")
    private String irpUrl;

    @JsonProperty("IDB_DisplayText")
    private String idbDisplayText;

    @JsonProperty("IsALF")
    private String isALF;

    @JsonProperty("EMSText")
    private String emsText;

    @JsonProperty("EMSURL")
    private String emsUrl;
}
