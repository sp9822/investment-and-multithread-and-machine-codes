package org.example.invest.dto.nse.etf;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.example.invest.dto.bse.index.BseRealTimeData;
import org.example.invest.dto.nse.index.NseIndexData;

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

    @JsonProperty("processedAssets")
    private String processedAssets;

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

    @JsonProperty("nearWKL")
    private Double nearWKL;

    @JsonProperty("latestToYearLowDiffPer")
    private Double latestToYearLowDiffPer;

    @JsonProperty("yearHighToYearLowDiffPer")
    private Double yearHighToYearLowDiffPer;

    @JsonProperty("nearWKH")
    private Double nearWKH;

    @JsonProperty("wkhi")
    private Double wkhi;

    @JsonProperty("wklo")
    private Double wklo;

    @JsonProperty("chn")
    private Double chn;

    @JsonProperty("per")
    private Double per;

    @JsonProperty("prevClose")
    private String prevClose;

    @JsonProperty("chartTodayPath")
    private String chartTodayPath;

    /*@JsonProperty("latestToLastWeekDiffPer")
    private Double latestToLastWeekDiffPer;*/

    @JsonProperty("perChange30d")
    private Double perChange30d;

    @JsonProperty("date30dAgo")
    private String date30dAgo;

    @JsonProperty("chart30dPath")
    private String chart30dPath;

    @JsonProperty("perChange365d")
    private Double perChange365d;

    @JsonProperty("date365dAgo")
    private String date365dAgo;

    @JsonProperty("chart365dPath")
    private String chart365dPath;

    @JsonProperty("qty")
    private Long qty;

    @JsonProperty("trdVal")
    private String trdVal;

    @JsonProperty("nseIndexData")
    private NseIndexData nseIndexData;

    @JsonProperty("bseIndexData")
    private BseRealTimeData bseIndexData;

    @JsonProperty("meta")
    private EtfMeta meta;

    @JsonProperty("stockIndClosePrice")
    private String stockIndClosePrice;

    @JsonProperty("mpc")
    private Double mpc;

    @JsonProperty("xdt")
    private String xdt;

    @JsonProperty("cact")
    private String cact;

    @JsonProperty("ypc")
    private Double ypc;

    public String getCompanyName() {
        if (this.meta == null) {
            return StringUtils.EMPTY;
        }
        return this.meta.getCompanyName();
    }

    public void setProcessedcompanyName(String processedcompanyName) {
        if (this.meta == null) {
            this.meta = new EtfMeta();
        }
        this.meta.setProcessedcompanyName(processedcompanyName);
    }


    public String getProcessedCompanyName() {
        if (this.meta == null) {
            return StringUtils.EMPTY;
        }
        return this.meta.getProcessedcompanyName();
    }
}