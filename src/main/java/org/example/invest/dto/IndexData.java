package org.example.invest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

    private String processedIndex;

    @JsonProperty("indexSymbol")
    private String indexSymbol;

    private String processedIndexSymbol;

    @JsonProperty("latestToYearLowDiffPer")
    private Double latestToYearLowDiffPer;

    @JsonProperty("yearHighToLatestDiffPer")
    private Double yearHighToLatestDiffPer;

    @JsonProperty("yearLowToYearHighDiffPer")
    private Double yearLowToYearHighDiffPer;

    @JsonProperty("last")
    private Double last;

    @JsonProperty("percentChange")
    private Double percentChange;

    @JsonProperty("variation")
    private Double variation;

    @JsonProperty("low")
    private Double low;

    @JsonProperty("open")
    private Double open;

    @JsonProperty("high")
    private Double high;

    @JsonProperty("previousClose")
    private Double previousClose;

    @JsonProperty("previousDayVal")
    private Double previousDayVal;

    @JsonProperty("previousDay")
    private String previousDay;

    @JsonProperty("chartTodayPath")
    private String chartTodayPath;

    @JsonProperty("latestToLastWeekDiffPer")
    private Double latestToLastWeekDiffPer;

    @JsonProperty("oneWeekAgoVal")
    private Double oneWeekAgoVal;

    @JsonProperty("oneWeekAgo")
    private String oneWeekAgo;

    @JsonProperty("perChange30d")
    private Double perChange30d;

    @JsonProperty("oneMonthAgoVal")
    private Double oneMonthAgoVal;

    @JsonProperty("date30dAgo")
    private String date30dAgo;

    @JsonProperty("chart30dPath")
    private String chart30dPath;

    @JsonProperty("perChange365d")
    private Double perChange365d;

    @JsonProperty("oneYearAgoVal")
    private Double oneYearAgoVal;

    @JsonProperty("date365dAgo")
    private String date365dAgo;

    @JsonProperty("chart365dPath")
    private String chart365dPath;

    @JsonProperty("yearLow")
    private Double yearLow;

    @JsonProperty("yearHigh")
    private Double yearHigh;

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

    @JsonProperty("indicativeClose")
    private Double indicativeClose;
}


