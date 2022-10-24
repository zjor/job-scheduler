package com.github.zjor.scheduler.outputs;

import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;

@Slf4j
public class HttpOutput implements Output {

    private final Map<String, Object> args;

    public HttpOutput(Map<String, Object> args) {
        this.args = args;
    }

    @Override
    public void output(Object value) {
        var url = (String) args.get("url");
        var auth = (Map<String, Object>) args.getOrDefault("auth", Collections.emptyMap());
        var topic = (String) args.getOrDefault("topic", "qotd");
        var login = (String) auth.get("login");
        var password = (String) auth.get("password");

        var response = Unirest.post(url)
                .basicAuth(login, password)
                .header("Content-Type", "application/json")
                .body(new Mqtt2TelegramBotTransformer(topic).transform(value))
                .asJson().getBody();
        log.info("<= {}", response);
    }
}