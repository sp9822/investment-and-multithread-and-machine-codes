package org.example.invest.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.example.invest.dto.bse.etf.BseEtfResponse;
import org.example.invest.dto.nse.etf.NseEtfResponse;
import org.example.invest.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.List;

/**
 * Service for AI-powered ETF analysis using ChatGPT
 */
@Service
@Slf4j
public class AiAnalysisService {

    @Value("${openai.api.key:}")
    private String openaiApiKey;

    @Value("${openai.api.model:gpt-4}")
    private String openaiModel;

    @Value("${openai.api.timeout:60}")
    private int timeoutSeconds;


    public static final String etfAnalysisCommand = "Following json format data consists details of an ETF from Indian stock exchanges." +
            " Please analyse and provide investment insights: ";

    public static final String sysEtfsPrompt = "'System Prompt: You are a financial analyst specializing in ETF analysis. " +
            "Provide detailed, professional analysis of ETF data from Indian stock exchanges (NSE and BSE). " +
            "Focus on market trends, performance metrics, and investment insights. " +
            "Be objective and data-driven in your analysis.'";

    public static final String sysIndicesPrompt = "'System Prompt: You are a financial analyst specializing in Stock Exchange Index analysis. " +
            "Provide detailed, professional analysis of Index data from Indian stock exchanges (NSE and BSE). " +
            "Focus on market trends, performance metrics, and investment insights. " +
            "Be objective and data-driven in your analysis.'";

    public static final String USER_PROMPT = "User Prompt: ";

    /**
     * Get AI opinion on ETF data from NSE and BSE
     *
     * @param nseResponse NSE ETF response data
     * @param bseResponse BSE ETF response data
     * @return AI analysis as String
     */
    public String getAiOpinion(NseEtfResponse nseResponse, BseEtfResponse bseResponse) {
        try {
            if (openaiApiKey == null || openaiApiKey.trim().isEmpty()) {
                log.warn("OpenAI API key not configured, returning default analysis");
                return getDefaultAnalysis();
            }

            String prompt = buildAnalysisPrompt(nseResponse, bseResponse);
            log.info(sysEtfsPrompt
                    .concat(System.lineSeparator().concat(USER_PROMPT).concat(prompt)));

            OpenAiService service = new OpenAiService(openaiApiKey, Duration.ofSeconds(timeoutSeconds));

            ChatCompletionRequest chatRequest = ChatCompletionRequest.builder()
                    .model(openaiModel)
                    .messages(List.of(
                            new ChatMessage(ChatMessageRole.SYSTEM.value(), sysEtfsPrompt),
                            new ChatMessage(ChatMessageRole.USER.value(), prompt)
                    ))
                    .maxTokens(2000)
                    .temperature(0.3)
                    .build();

            String response = service.createChatCompletion(chatRequest)
                    .getChoices()
                    .get(0)
                    .getMessage()
                    .getContent();

            log.info("AI analysis completed successfully");
            return response;

        } catch (Exception e) {
            log.error("Error getting AI opinion: {}", e.getMessage(), e);
            return getDefaultAnalysis();
        }
    }

    /**
     * Build comprehensive prompt with ETF data
     */
    private String buildAnalysisPrompt(NseEtfResponse nseResponse, BseEtfResponse bseResponse) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Please analyze the following ETF data from Indian stock exchanges and provide investment insights:\n\n");

        // NSE Data Summary
        prompt.append("=== NSE ETF DATA ===\n");
        if (nseResponse != null) {
            prompt.append("Timestamp: ").append(nseResponse.getTimestamp()).append("\n");
            prompt.append("NAV Date: ").append(nseResponse.getNavDate()).append("\n");
            prompt.append("Total ETFs: ").append(nseResponse.getData() != null ? nseResponse.getData().size() : 0).append("\n");
            prompt.append("Market Status: ").append(nseResponse.getMarketStatus()).append("\n");

            if (!CollectionUtils.isEmpty(nseResponse.getData())) {
                prompt.append("\nFollowing contains NSE ETFs data:\n");
                prompt.append(ObjectUtils.objectToJsonString(nseResponse.getData()));
            }
        } else {
            prompt.append("No NSE data available\n");
        }

        prompt.append("\n=== BSE ETF DATA ===\n");
        if (bseResponse != null) {
            prompt.append("Timestamp: ").append(bseResponse.getTimestamp()).append("\n");
            prompt.append("Total ETFs: ").append(bseResponse.getData() != null ? bseResponse.getData().size() : 0).append("\n");

            if (!CollectionUtils.isEmpty(bseResponse.getData())) {
                prompt.append("\nFollowing contains BSE ETFs data:\n");
                prompt.append(ObjectUtils.objectToJsonString(bseResponse.getData()));
            }
        } else {
            prompt.append("No BSE data available\n");
        }

        prompt.append("\n=== ANALYSIS REQUEST ===\n");
        prompt.append("Please provide:\n");
        prompt.append("1. Overall market sentiment analysis\n");
        prompt.append("2. Key performance trends and patterns\n");
        prompt.append("3. Top performing ETFs and their characteristics\n");
        prompt.append("4. Risk assessment and volatility indicators\n");
        prompt.append("5. Investment recommendations based on current data\n");
        prompt.append("6. Market outlook and potential opportunities\n");
        prompt.append("\nPlease keep the analysis professional, data-driven, and suitable for investment decision-making.");

        return prompt.toString();
    }

    /**
     * Default analysis when AI service is unavailable
     */
    private String getDefaultAnalysis() {
        return "AI Analysis Service Unavailable\n\n" +
                "The AI analysis service is currently unavailable. " +
                "Please check the configuration and ensure the OpenAI API key is properly set. " +
                "For investment decisions, please consult with a qualified financial advisor " +
                "and perform your own due diligence based on the provided ETF data.";
    }
}

