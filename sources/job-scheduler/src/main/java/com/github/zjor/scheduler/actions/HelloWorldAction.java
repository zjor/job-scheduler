package com.github.zjor.scheduler.actions;

import com.github.zjor.scheduler.SchedulerService;
import com.github.zjor.scheduler.outputs.Output;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.support.CronExpression;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
public class HelloWorldAction extends Action {

    public HelloWorldAction(String jobId,
                            SchedulerService executor,
                            CronExpression cron,
                            Map<String, Object> args,
                            List<Output> outputs) {
        super(jobId, executor, cron, args, outputs);
    }

    @Override
    public String getName() {
        return "HELLO_WORLD";
    }

    @Override
    public void execute(Map<String, Object> args) {
        var now = LocalDateTime.now();
        log.info("Called at {}; args: {}", now, args);
    }
}
