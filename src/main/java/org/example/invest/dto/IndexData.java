package org.example.invest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * DTO for individual index data in NSE allIndices API response
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class IndexData {
    
    @JsonProperty("key")
    private String key;
    
    @JsonProperty("index")
    private String index;
    
    @JsonProperty("indexSymbol")
    private String indexSymbol;

    @JsonProperty("latestToYearLowDiffPer")
    private Double latestToYearLowDiffPer;

    @JsonProperty("yearLowToYearHighDiffPer")
    private Double yearLowToYearHighDiffPer;

    @JsonProperty("last")
    private Double last;

    @JsonProperty("percentChange")
    private Double percentChange;

    @JsonProperty("variation")
    private Double variation;

    @JsonProperty("previousClose")
    private Double previousClose;

    @JsonProperty("chartTodayPath")
    private String chartTodayPath;

    @JsonProperty("latestToLastWeekDiffPer")
    private Double latestToLastWeekDiffPer;

    @JsonProperty("perChange30d")
    private Double perChange30d;

    @JsonProperty("chart30dPath")
    private String chart30dPath;

    @JsonProperty("perChange365d")
    private Double perChange365d;

    @JsonProperty("chart365dPath")
    private String chart365dPath;

    @JsonProperty("low")
    private Double low;

    @JsonProperty("open")
    private Double open;
    
    @JsonProperty("high")
    private Double high;

    @JsonProperty("yearLow")
    private Double yearLow;

    @JsonProperty("yearHigh")
    private Double yearHigh;
    
    @JsonProperty("indicativeClose")
    private Double indicativeClose;
    
    @JsonProperty("pe")
    private Double pe;
    
    @JsonProperty("pb")
    private Double pb;
    
    @JsonProperty("dy")
    private Double dy;
    
    @JsonProperty("declines")
    private Long declines;
    
    @JsonProperty("advances")
    private Long advances;
    
    @JsonProperty("unchanged")
    private Long unchanged;

    @JsonProperty("previousDayVal")
    private Double previousDayVal;

    @JsonProperty("previousDay")
    private String previousDay;

    @JsonProperty("oneWeekAgoVal")
    private Double oneWeekAgoVal;

    @JsonProperty("oneWeekAgo")
    private String oneWeekAgo;

    @JsonProperty("oneMonthAgoVal")
    private Double oneMonthAgoVal;

    @JsonProperty("date30dAgo")
    private String date30dAgo;

    @JsonProperty("oneYearAgoVal")
    private Double oneYearAgoVal;

    @JsonProperty("date365dAgo")
    private String date365dAgo;
}

