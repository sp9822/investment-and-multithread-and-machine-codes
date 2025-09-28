package org.example.invest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for date information in NSE allIndices API response
 */
public class DateInfo {
    
    @JsonProperty("previousDay")
    private String previousDay;
    
    @JsonProperty("oneWeekAgo")
    private String oneWeekAgo;
    
    @JsonProperty("oneMonthAgo")
    private String oneMonthAgo;
    
    @JsonProperty("oneYearAgo")
    private String oneYearAgo;
    
    // Constructors
    public DateInfo() {}
    
    // Getters and Setters
    public String getPreviousDay() {
        return previousDay;
    }
    
    public void setPreviousDay(String previousDay) {
        this.previousDay = previousDay;
    }
    
    public String getOneWeekAgo() {
        return oneWeekAgo;
    }
    
    public void setOneWeekAgo(String oneWeekAgo) {
        this.oneWeekAgo = oneWeekAgo;
    }
    
    public String getOneMonthAgo() {
        return oneMonthAgo;
    }
    
    public void setOneMonthAgo(String oneMonthAgo) {
        this.oneMonthAgo = oneMonthAgo;
    }
    
    public String getOneYearAgo() {
        return oneYearAgo;
    }
    
    public void setOneYearAgo(String oneYearAgo) {
        this.oneYearAgo = oneYearAgo;
    }
    
    @Override
    public String toString() {
        return "DateInfo{" +
                "previousDay='" + previousDay + '\'' +
                ", oneWeekAgo='" + oneWeekAgo + '\'' +
                ", oneMonthAgo='" + oneMonthAgo + '\'' +
                ", oneYearAgo='" + oneYearAgo + '\'' +
                '}';
    }
}

