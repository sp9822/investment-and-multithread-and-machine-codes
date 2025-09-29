package org.example.invest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * DTO for ETF metadata in NSE ETF API response
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class EtfMeta {

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("companyName")
    private String companyName;

    @JsonProperty("processedcompanyName")
    private String processedcompanyName;

    @JsonProperty("industry")
    private String industry;

    @JsonProperty("activeSeries")
    private List<String> activeSeries;

    @JsonProperty("debtSeries")
    private List<String> debtSeries;

    @JsonProperty("isFNOSec")
    private Boolean isFNOSec;

    @JsonProperty("isCASec")
    private Boolean isCASec;

    @JsonProperty("isSLBSec")
    private Boolean isSLBSec;

    @JsonProperty("isDebtSec")
    private Boolean isDebtSec;

    @JsonProperty("isSuspended")
    private Boolean isSuspended;

    @JsonProperty("tempSuspendedSeries")
    private List<String> tempSuspendedSeries;

    @JsonProperty("isETFSec")
    private Boolean isETFSec;

    @JsonProperty("isDelisted")
    private Boolean isDelisted;

    @JsonProperty("isin")
    private String isin;

    @JsonProperty("slb_isin")
    private String slbIsin;

    @JsonProperty("listingDate")
    private String listingDate;

    @JsonProperty("isMunicipalBond")
    private Boolean isMunicipalBond;

    @JsonProperty("isHybridSymbol")
    private Boolean isHybridSymbol;

    @JsonProperty("quotepreopenstatus")
    private QuotePreOpenStatus quotepreopenstatus;
}
