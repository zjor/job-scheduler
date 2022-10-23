package com.github.zjor.scheduler.outputs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.Unirest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;

@Slf4j
public class HttpOutput implements Output {

    private final Map<String, Object> args;
    private final ObjectMapper mapper = new ObjectMapper();

    public HttpOutput(Map<String, Object> args) {
        this.args = args;
    }

    @Override
    public void output(Object value) {
        var url = (String) args.get("url");
        var auth = (Map<String, Object>) args.getOrDefault("auth", Collections.emptyMap());
        var login = (String) auth.get("login");
        var password = (String) auth.get("password");
        try {
            var req = RequestBody.builder()
                    .topic("qotd")
                    .payload(String.valueOf(value))
                    .build();
            var response = Unirest.post(url)
                    .basicAuth(login, password)
                    .header("Content-Type", "application/json")
                    .body(mapper.writeValueAsString(req))
                    .asJson().getBody();
            log.info("<= {}", response);
        } catch (JsonProcessingException e) {
            log.error("Failed to invoke HTTP output: " + e.getMessage(), e);
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestBody {
        private String topic;
        private String payload;
    }
}