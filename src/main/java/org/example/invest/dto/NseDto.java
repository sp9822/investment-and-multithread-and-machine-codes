package org.example.invest.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.invest.dto.nse.etf.NseEtfResponse;
import org.example.invest.dto.nse.index.NseAllIndicesResponse;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class NseDto {
    private String nseIndiaCookie;

    private NseAllIndicesResponse allIndices;
    private NseAllIndicesResponse investableIndices;

    private NseEtfResponse allEtf;
    private NseEtfResponse investableEtf;
}
