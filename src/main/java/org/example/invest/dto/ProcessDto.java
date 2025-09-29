package org.example.invest.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ProcessDto {
    NseAllIndicesResponse allIndices;
    NseAllIndicesResponse investableIndices;

    NseEtfResponse allEtf;
    NseEtfResponse investableEtf;
}
