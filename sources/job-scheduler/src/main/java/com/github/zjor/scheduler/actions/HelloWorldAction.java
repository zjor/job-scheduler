package com.github.zjor.scheduler.actions;

import com.github.zjor.scheduler.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.support.CronExpression;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
public class HelloWorldAction extends Action {

    public HelloWorldAction(String jobId, SchedulerService executor, CronExpression cron, Map<String, Object> args) {
        super(jobId, executor, cron, args);
    }

    @Override
    public String getName() {
        return "HELLO_WORLD";
    }

    @Override
    public void run(Map<String, Object> args) {
        var now = LocalDateTime.now();
        log.info("Called at {}; args: {}", now, args);
    }
}
