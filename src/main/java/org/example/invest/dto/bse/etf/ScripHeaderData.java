package org.example.invest.dto.bse.etf;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ScripHeaderData {
    @JsonProperty("CurrRate")
    private CurrRate currRate;

    @JsonProperty("Cmpname")
    private Cmpname cmpname;

    @JsonProperty("Header")
    private Header header;

    @JsonProperty("CompResp")
    private CompResp compResp;
}