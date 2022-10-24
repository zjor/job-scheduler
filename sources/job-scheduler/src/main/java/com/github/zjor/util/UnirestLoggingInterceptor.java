package com.github.zjor.util;

import kong.unirest.Config;
import kong.unirest.HttpRequest;
import kong.unirest.HttpRequestSummary;
import kong.unirest.HttpResponse;
import kong.unirest.Interceptor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnirestLoggingInterceptor implements Interceptor {
    @Override
    public void onRequest(HttpRequest<?> request, Config config) {
        String headers = request.getHeaders().toString().replaceAll("[\r\n]+", "; ");
        log.info("{} {} [{}]; => {}: {}", request.getHttpMethod(), request.getUrl(), headers, request.getUrl(),
                request.getBody().orElse(null));
    }

    @Override
    public void onResponse(HttpResponse<?> response, HttpRequestSummary request, Config config) {
        log.info("[{}:{}] <= {}", response.getStatus(), response.getStatusText(), response.getBody());
    }
}