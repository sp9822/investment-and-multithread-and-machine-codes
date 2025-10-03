package org.example.invest.dto.nse.index;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.invest.dto.DateInfo;

import java.util.List;

/**
 * DTO for NSE allIndices API response
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class NseAllIndicesResponse {

    @JsonProperty("data")
    private List<NseIndexData> data;

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
}


