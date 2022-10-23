package com.github.zjor.scheduler.outputs;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingOutput implements Output {
    @Override
    public void output(Object value) {
        log.info("Value: {}", value);
    }
}
