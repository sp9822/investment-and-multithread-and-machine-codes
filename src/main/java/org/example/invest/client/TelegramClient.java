package org.example.invest.client;

import org.example.invest.dto.TelegramMessageRequest;
import org.example.invest.util.GeneralUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TelegramClient {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private GeneralUtil generalUtil;


    public void sendMsg(String botToken, Long chatId, String aiPrompt) {
        if (aiPrompt.length() > 4096) {
            sendMsg(botToken, chatId, aiPrompt.substring(0, 4096));
            sendMsg(botToken, chatId, aiPrompt.substring(4096));
        } else {
            String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";

            // Create request payload
            TelegramMessageRequest request = new TelegramMessageRequest(chatId, aiPrompt);

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create HTTP entity
            HttpEntity<TelegramMessageRequest> entity = new HttpEntity<>(request, headers);

            try {
                // Send POST request to Telegram API
                restTemplate.postForObject(url, entity, String.class);
            } catch (Exception e) {
                // Log error or handle as needed
                System.err.println("Error sending Telegram message: " + e.getMessage());
                throw new RuntimeException("Failed to send Telegram message", e);
            }
        }
    }
}
