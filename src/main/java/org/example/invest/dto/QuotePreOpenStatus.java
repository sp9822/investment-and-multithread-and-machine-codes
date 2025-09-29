package org.example.invest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO for quote pre-open status in ETF metadata
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class QuotePreOpenStatus {

    @JsonProperty("equityTime")
    private String equityTime;

    @JsonProperty("preOpenTime")
    private String preOpenTime;

    @JsonProperty("QuotePreOpenFlag")
    private Boolean quotePreOpenFlag;
}
