package com.github.zjor.scheduler.outputs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Transforms payload to be eligible for sending to Mqtt2TelegramBot
 */
@Slf4j
public class Mqtt2TelegramBotTransformer {

    private final ObjectMapper mapper = new ObjectMapper();

    private final String topic;

    public Mqtt2TelegramBotTransformer(String topic) {
        this.topic = topic;
    }

    public String transform(Object value) {
        Map<String, String> payload = new HashMap<>();
        payload.put("topic", topic);
        payload.put("payload", String.valueOf(value));
        try {
            return mapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize to JSON: " + e.getMessage(), e);
        }
    }

}
