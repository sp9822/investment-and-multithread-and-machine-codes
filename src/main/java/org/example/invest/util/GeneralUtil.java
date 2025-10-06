package org.example.invest.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import static org.example.Constants.HUNDREAD_D;
import static org.example.Constants.ZERO_D;

@Component
public class GeneralUtil {
    public String removeHypnUnderScorSpcSecIndAndGetLowerCase(String str) {
        if (StringUtils.isEmpty(str)) {
            return StringUtils.EMPTY;
        }
        String subStr = str.toLowerCase();
        subStr = subStr.replaceAll("-", "");
        subStr = subStr.replaceAll("_", "");
        subStr = subStr.replaceAll(" ", "");
        subStr = subStr.replaceAll("sector", "");
        subStr = subStr.replaceAll("sec", "");
        subStr = subStr.replaceAll("index", "");
        subStr = subStr.replaceAll("etf", "");
        subStr = subStr.replaceAll("exchange", "");
        subStr = subStr.replaceAll("traded", "");
        subStr = subStr.replaceAll("trade", "");
        subStr = subStr.replaceAll("fund", "");
        subStr = subStr.replaceAll("india", "");
        return subStr;
    }

    public Double getDelta(Double val1, Double val2) {
        if (val1 == null || val2 == null) {
            return ZERO_D;
        }
        return val1 - val2;
    }

    public Double getDeltaPercent(Double numerator, Double denomerator) {
        if (numerator == null || denomerator == null || ZERO_D.equals(denomerator)) {
            return Double.MAX_VALUE;
        }
        return ((numerator / denomerator * HUNDREAD_D) - HUNDREAD_D);
    }
}
