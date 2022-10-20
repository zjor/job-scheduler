package com.github.zjor.scheduler;

import com.github.zjor.spring.aop.Log;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SchedulerService {

    @Log
    public void f() {
        log.info("called");
    }
}
