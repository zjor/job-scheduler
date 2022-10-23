package com.github.zjor.scheduler.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.function.Supplier;

public class ControllerUtils {
    public static Supplier<ResponseStatusException> notFound(String value) {
        return () -> new ResponseStatusException(HttpStatus.NOT_FOUND, value);
    }
}
