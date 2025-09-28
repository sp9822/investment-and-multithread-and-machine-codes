package org.example.invest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * DTO for NSE allIndices API response
 */
public class NseAllIndicesResponse {
    
    @JsonProperty("data")
    private List<IndexData> data;
    
    @JsonProperty("timestamp")
    private String timestamp;
    
    @JsonProperty("advances")
    private Integer advances;
    
    @JsonProperty("declines")
    private Integer declines;
    
    @JsonProperty("unchanged")
    private Integer unchanged;
    
    @JsonProperty("dates")
    private DateInfo dates;
    
    // Constructors
    public NseAllIndicesResponse() {}
    
    // Getters and Setters
    public List<IndexData> getData() {
        return data;
    }
    
    public void setData(List<IndexData> data) {
        this.data = data;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    public Integer getAdvances() {
        return advances;
    }
    
    public void setAdvances(Integer advances) {
        this.advances = advances;
    }
    
    public Integer getDeclines() {
        return declines;
    }
    
    public void setDeclines(Integer declines) {
        this.declines = declines;
    }
    
    public Integer getUnchanged() {
        return unchanged;
    }
    
    public void setUnchanged(Integer unchanged) {
        this.unchanged = unchanged;
    }
    
    public DateInfo getDates() {
        return dates;
    }
    
    public void setDates(DateInfo dates) {
        this.dates = dates;
    }
    
    @Override
    public String toString() {
        return "NseAllIndicesResponse{" +
                "data=" + data +
                ", timestamp='" + timestamp + '\'' +
                ", advances=" + advances +
                ", declines=" + declines +
                ", unchanged=" + unchanged +
                ", dates=" + dates +
                '}';
    }
}

