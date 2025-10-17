package org.example.invest.dto.bse.etf;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CompResp {
    @JsonProperty("compRes")
    private Object compRes;

    @JsonProperty("texturl")
    private String textUrl;
}
