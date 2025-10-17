package org.example.invest.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class ObjectUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public static <T> String objectToJsonString(T object) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object).replaceAll("[\r\n]+", " ");
        } catch (Exception e) {
            log.error("exception\n" + e);
            log.error("JsonProcessingException in objectToJsonString {}", object);
        }
        return null;
    }

    public static <T> T jsonToObject(String json, Class<T> expectedObject) {
        try {
            return objectMapper.readValue(json, expectedObject);
        } catch (Exception e) {
            log.error("JsonProcessingException in jsonToObject, error={}, json={}", e.getMessage(), json, e);
        }
        return null;
    }

    public static <T> T jsonToObject(String json, TypeReference<T> expectedObject) {
        try {
            return objectMapper.readValue(json, expectedObject);
        } catch (IOException e) {
            log.error("JsonProcessingException in jsonToObject, error={}, json={}", e.getMessage(), json, e);
        }
        return null;
    }
}