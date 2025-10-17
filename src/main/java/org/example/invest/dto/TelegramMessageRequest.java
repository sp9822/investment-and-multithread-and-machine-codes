package org.example.invest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TelegramMessageRequest {
    @JsonProperty("chat_id")
    private Long chatId;

    @JsonProperty("text")
    private String text;
}
