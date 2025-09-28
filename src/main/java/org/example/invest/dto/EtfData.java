package org.example.invest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

/**
 * DTO for individual ETF data in NSE ETF API response
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class EtfData {
    @JsonProperty("symbol")
    private String symbol;
    
    @JsonProperty("assets")
    private String assets;
    
    @JsonProperty("open")
    private String open;
    
    @JsonProperty("high")
    private String high;
    
    @JsonProperty("low")
    private String low;
    
    @JsonProperty("ltP")
    private Double ltP;

    @JsonProperty("nav")
    private Double nav;

    @JsonProperty("navToMarketLtPDelta")
    private Double navToMarketLtPDelta;

    @JsonProperty("navToMarketLtPDeltaPercent")
    private Double navToMarketLtPDeltaPercent;

    @JsonProperty("chn")
    private String chn;
    
    @JsonProperty("per")
    private Double per;
    
    @JsonProperty("qty")
    private Long qty;
    
    @JsonProperty("trdVal")
    private String trdVal;
    
    @JsonProperty("wkhi")
    private String wkhi;
    
    @JsonProperty("wklo")
    private String wklo;
    
    @JsonProperty("prevClose")
    private String prevClose;
    
    @JsonProperty("stockIndClosePrice")
    private String stockIndClosePrice;
    
    @JsonProperty("perChange365d")
    private Double perChange365d;
    
    @JsonProperty("perChange30d")
    private Double perChange30d;
    
    @JsonProperty("date365dAgo")
    private String date365dAgo;
    
    @JsonProperty("date30dAgo")
    private String date30dAgo;
    
    @JsonProperty("mpc")
    private Double mpc;
    
    @JsonProperty("xdt")
    private String xdt;
    
    @JsonProperty("cact")
    private String cact;
    
    @JsonProperty("ypc")
    private Double ypc;
    
    @JsonProperty("nearWKH")
    private Double nearWKH;
    
    @JsonProperty("nearWKL")
    private Double nearWKL;
    
    @JsonProperty("chartTodayPath")
    private String chartTodayPath;
    
    @JsonProperty("chart30dPath")
    private String chart30dPath;
    
    @JsonProperty("chart365dPath")
    private String chart365dPath;
    
    @JsonProperty("meta")
    private EtfMeta meta;

    public String getCompanyName() {
        if (this.meta == null) {
            return StringUtils.EMPTY;
        }
        return this.meta.getCompanyName();
    }
}
