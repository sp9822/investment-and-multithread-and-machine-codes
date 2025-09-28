package org.example.invest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * DTO for ETF metadata in NSE ETF API response
 */
public class EtfMeta {
    
    @JsonProperty("symbol")
    private String symbol;
    
    @JsonProperty("companyName")
    private String companyName;
    
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
    
    // Constructors
    public EtfMeta() {}
    
    // Getters and Setters
    public String getSymbol() {
        return symbol;
    }
    
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public String getIndustry() {
        return industry;
    }
    
    public void setIndustry(String industry) {
        this.industry = industry;
    }
    
    public List<String> getActiveSeries() {
        return activeSeries;
    }
    
    public void setActiveSeries(List<String> activeSeries) {
        this.activeSeries = activeSeries;
    }
    
    public List<String> getDebtSeries() {
        return debtSeries;
    }
    
    public void setDebtSeries(List<String> debtSeries) {
        this.debtSeries = debtSeries;
    }
    
    public Boolean getIsFNOSec() {
        return isFNOSec;
    }
    
    public void setIsFNOSec(Boolean isFNOSec) {
        this.isFNOSec = isFNOSec;
    }
    
    public Boolean getIsCASec() {
        return isCASec;
    }
    
    public void setIsCASec(Boolean isCASec) {
        this.isCASec = isCASec;
    }
    
    public Boolean getIsSLBSec() {
        return isSLBSec;
    }
    
    public void setIsSLBSec(Boolean isSLBSec) {
        this.isSLBSec = isSLBSec;
    }
    
    public Boolean getIsDebtSec() {
        return isDebtSec;
    }
    
    public void setIsDebtSec(Boolean isDebtSec) {
        this.isDebtSec = isDebtSec;
    }
    
    public Boolean getIsSuspended() {
        return isSuspended;
    }
    
    public void setIsSuspended(Boolean isSuspended) {
        this.isSuspended = isSuspended;
    }
    
    public List<String> getTempSuspendedSeries() {
        return tempSuspendedSeries;
    }
    
    public void setTempSuspendedSeries(List<String> tempSuspendedSeries) {
        this.tempSuspendedSeries = tempSuspendedSeries;
    }
    
    public Boolean getIsETFSec() {
        return isETFSec;
    }
    
    public void setIsETFSec(Boolean isETFSec) {
        this.isETFSec = isETFSec;
    }
    
    public Boolean getIsDelisted() {
        return isDelisted;
    }
    
    public void setIsDelisted(Boolean isDelisted) {
        this.isDelisted = isDelisted;
    }
    
    public String getIsin() {
        return isin;
    }
    
    public void setIsin(String isin) {
        this.isin = isin;
    }
    
    public String getSlbIsin() {
        return slbIsin;
    }
    
    public void setSlbIsin(String slbIsin) {
        this.slbIsin = slbIsin;
    }
    
    public String getListingDate() {
        return listingDate;
    }
    
    public void setListingDate(String listingDate) {
        this.listingDate = listingDate;
    }
    
    public Boolean getIsMunicipalBond() {
        return isMunicipalBond;
    }
    
    public void setIsMunicipalBond(Boolean isMunicipalBond) {
        this.isMunicipalBond = isMunicipalBond;
    }
    
    public Boolean getIsHybridSymbol() {
        return isHybridSymbol;
    }
    
    public void setIsHybridSymbol(Boolean isHybridSymbol) {
        this.isHybridSymbol = isHybridSymbol;
    }
    
    public QuotePreOpenStatus getQuotepreopenstatus() {
        return quotepreopenstatus;
    }
    
    public void setQuotepreopenstatus(QuotePreOpenStatus quotepreopenstatus) {
        this.quotepreopenstatus = quotepreopenstatus;
    }
    
    @Override
    public String toString() {
        return "EtfMeta{" +
                "symbol='" + symbol + '\'' +
                ", companyName='" + companyName + '\'' +
                ", industry='" + industry + '\'' +
                ", activeSeries=" + activeSeries +
                ", debtSeries=" + debtSeries +
                ", isFNOSec=" + isFNOSec +
                ", isCASec=" + isCASec +
                ", isSLBSec=" + isSLBSec +
                ", isDebtSec=" + isDebtSec +
                ", isSuspended=" + isSuspended +
                ", tempSuspendedSeries=" + tempSuspendedSeries +
                ", isETFSec=" + isETFSec +
                ", isDelisted=" + isDelisted +
                ", isin='" + isin + '\'' +
                ", slbIsin='" + slbIsin + '\'' +
                ", listingDate='" + listingDate + '\'' +
                ", isMunicipalBond=" + isMunicipalBond +
                ", isHybridSymbol=" + isHybridSymbol +
                ", quotepreopenstatus=" + quotepreopenstatus +
                '}';
    }
}
