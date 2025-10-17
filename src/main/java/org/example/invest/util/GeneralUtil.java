package org.example.invest.util;

import org.apache.commons.lang3.StringUtils;
import org.example.invest.dto.bse.etf.BseEtfData;
import org.example.invest.dto.bse.index.BseRealTimeData;
import org.example.invest.dto.nse.etf.EtfData;
import org.example.invest.dto.nse.index.NseIndexData;
import org.example.invest.service.AiAnalysisService;
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
        subStr = subStr.replaceAll("exchange", "");
        subStr = subStr.replaceAll("etf", "");
        subStr = subStr.replaceAll("fund", "");
        subStr = subStr.replaceAll("index", "");
        subStr = subStr.replaceAll("india", "");
        subStr = subStr.replaceAll("mutual", "");
        subStr = subStr.replaceAll("mf", "");
        subStr = subStr.replaceAll("sector", "");
        subStr = subStr.replaceAll("sec", "");
        subStr = subStr.replaceAll("traded", "");
        subStr = subStr.replaceAll("trade", "");
        return subStr;
    }

    public Double getDelta(Double val1, Double val2) {
        if (val1 == null || val2 == null) {
            return null;
        }
        return val1 - val2;
    }

    public Double getDeltaPercent(Double numerator, Double denomerator) {
        if (numerator == null || denomerator == null || ZERO_D.equals(denomerator)) {
            return null;
        }
        return ((numerator / denomerator * HUNDREAD_D) - HUNDREAD_D);
    }

    public Double getMedian(Double high, Double low) {
        if (high == null || low == null) {
            return null;
        }
        return ((high + low) / 2.0);
    }

    public <T> int compareWithNullInLast(Object d1, Object d2) {
        if (d1 != null && d2 != null) {
            if (d1 instanceof Double) {
                return Double.compare((double) d1, (double) d2);
            }
            if (d1 instanceof Long) {
                return Long.compare((long) d1, (long) d2);
            }
        }
        if (d1 != null) {
            return -1;
        }
        if (d2 != null) {
            return 1;
        }
        return 0;
    }

    public static final String noEtf = "No ETF data available";

    public static final String analysisRequest = new StringBuilder("\n=== ANALYSIS REQUEST ===\n")
            .append("Please provide:\n")
            .append("1. Overall market sentiment analysis\n")
            .append("2. Key performance trends and patterns\n")
            .append("3. Top performing ETFs and their characteristics\n")
            .append("4. Risk assessment and volatility indicators\n")
            .append("5. Investment recommendations based on current data\n")
            .append("6. Market outlook and potential opportunities\n")
            .append("7. Final decision for investment: Seedhe btao invest kru ya nhi.. Lumpsump ya Gradually? \n")
            .append("\nPlease keep the analysis professional, data-driven, and suitable for investment decision-making.")
            .toString();

    private String getUserPrompt(Object etfData) {
        if (etfData == null) {
            return noEtf;
        }
        return new StringBuilder("'").append(AiAnalysisService.USER_PROMPT).append(AiAnalysisService.etfAnalysisCommand)
                .append(System.lineSeparator()).append(ObjectUtils.objectToJsonString(etfData))
                .append(System.lineSeparator()).append(analysisRequest).append("'").toString();
    }

    public String generateAiPrompt(EtfData nseEtf) {
        return new StringBuilder(AiAnalysisService.sysEtfsPrompt).append(System.lineSeparator())
                .append(System.lineSeparator()).append(getUserPrompt(nseEtf)).toString();
    }

    public String generateAiPrompt(BseEtfData bseEtf) {
        return new StringBuilder(AiAnalysisService.sysEtfsPrompt).append(System.lineSeparator())
                .append(System.lineSeparator()).append(getUserPrompt(bseEtf)).toString();
    }

    public String generateAiPrompt(NseIndexData nseIndex) {
        return new StringBuilder(AiAnalysisService.sysIndicesPrompt).append(System.lineSeparator())
                .append(System.lineSeparator()).append(getIndexUserPrompt(nseIndex)).toString();
    }

    public String generateAiPrompt(BseRealTimeData bseIndex) {
        return new StringBuilder(AiAnalysisService.sysIndicesPrompt).append(System.lineSeparator())
                .append(System.lineSeparator()).append(getIndexUserPrompt(bseIndex)).toString();
    }

    public static final String noIndex = "No Index data available";

    public static final String indexAnalysisRequest = new StringBuilder("\n=== ANALYSIS REQUEST ===\n")
            .append("Please provide:\n")
            .append("1. Overall market sentiment analysis\n")
            .append("2. Key performance trends and patterns\n")
            .append("3. Top performing indices and their characteristics\n")
            .append("4. Risk assessment and volatility indicators\n")
            .append("5. Market outlook and potential opportunities\n")
            .append("6. Sector-wise performance analysis\n")
            .append("7. Investment recommendations based on current index data\n")
            .append("8. Final decision for investment: Seedhe btao invest kru ya nhi.. Lumpsump ya Gradually? \n")
            .append("\nPlease keep the analysis professional, data-driven, and suitable for investment decision-making.")
            .toString();

    private String getIndexUserPrompt(Object indexData) {
        if (indexData == null) {
            return noIndex;
        }
        String indexAnalysisCommand = "Following json format data consists details of an Index from Indian stock exchanges." +
                " Please analyse and provide investment insights: ";
        return new StringBuilder("'").append(AiAnalysisService.USER_PROMPT).append(indexAnalysisCommand)
                .append(System.lineSeparator()).append(ObjectUtils.objectToJsonString(indexData))
                .append(System.lineSeparator()).append(indexAnalysisRequest).append("'").toString();
    }
}
