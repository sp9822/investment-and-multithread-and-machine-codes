package org.example.invest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for quote pre-open status in ETF metadata
 */
public class QuotePreOpenStatus {
    
    @JsonProperty("equityTime")
    private String equityTime;
    
    @JsonProperty("preOpenTime")
    private String preOpenTime;
    
    @JsonProperty("QuotePreOpenFlag")
    private Boolean quotePreOpenFlag;
    
    // Constructors
    public QuotePreOpenStatus() {}
    
    // Getters and Setters
    public String getEquityTime() {
        return equityTime;
    }
    
    public void setEquityTime(String equityTime) {
        this.equityTime = equityTime;
    }
    
    public String getPreOpenTime() {
        return preOpenTime;
    }
    
    public void setPreOpenTime(String preOpenTime) {
        this.preOpenTime = preOpenTime;
    }
    
    public Boolean getQuotePreOpenFlag() {
        return quotePreOpenFlag;
    }
    
    public void setQuotePreOpenFlag(Boolean quotePreOpenFlag) {
        this.quotePreOpenFlag = quotePreOpenFlag;
    }
    
    @Override
    public String toString() {
        return "QuotePreOpenStatus{" +
                "equityTime='" + equityTime + '\'' +
                ", preOpenTime='" + preOpenTime + '\'' +
                ", quotePreOpenFlag=" + quotePreOpenFlag +
                '}';
    }
}
