package com.github.zjor.scheduler.actions;

import com.github.zjor.scheduler.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.support.CronExpression;

import java.time.LocalDateTime;

@Slf4j
public class HelloWorldAction extends Action {

    public HelloWorldAction(String jobId, SchedulerService executor, CronExpression cron) {
        super(jobId, executor, cron);
    }

    @Override
    public void run() {
        var now = LocalDateTime.now();
        log.info("called: {}", now);
    }
}
