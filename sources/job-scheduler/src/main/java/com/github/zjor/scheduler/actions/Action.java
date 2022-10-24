package com.github.zjor.scheduler.actions;

import com.github.zjor.scheduler.SchedulerService;
import com.github.zjor.scheduler.outputs.Output;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.support.CronExpression;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class Action {

    public static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;

    private final String jobId;
    private final CronExpression cron;
    private final Map<String, Object> args;

    protected final List<Output> outputs;

    protected final SchedulerService schedulerService;
    protected final Environment environment;

    // TODO: code smell: action knows the whole job internals
    protected Action(ApplicationContext context,
                     String jobId,
                     CronExpression cron,
                     Map<String, Object> args,
                     List<Output> outputs) {
        this.jobId = jobId;
        this.cron = cron;
        this.args = args;
        this.outputs = outputs;

        this.schedulerService = context.getBean(SchedulerService.class);
        this.environment = context.getBean(Environment.class);
    }

    protected long getDelay() {
        var now = LocalDateTime.now();
        var next = cron.next(now);
        return timeDifferenceMillis(now, next);
    }

    public void scheduleNext() {
        var future = schedulerService.getExecutorService().schedule(this::invoke, getDelay(), TIME_UNIT);
        schedulerService.getSchedule().put(jobId, future);
    }

    private void invoke() {
        log.info("Running job {}; ID: {}", getName(), jobId);
        var now = System.currentTimeMillis();
        try {
            execute(args);
        } catch (Throwable t) {
            log.error("Job execution failed: " + t.getMessage(), t);
        } finally {
            var elapsed = System.currentTimeMillis() - now;
            log.info("Job (name: {}, ID: {}): elapsed time: {}ms", getName(), jobId, elapsed);
        }
        scheduleNext();
    }

    private long toEpochMillis(LocalDateTime time) {
        ZonedDateTime zdt = ZonedDateTime.of(time, ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli();
    }

    private long timeDifferenceMillis(LocalDateTime now, LocalDateTime then) {
        return toEpochMillis(then) - toEpochMillis(now);
    }

    public abstract String getName();

    protected abstract void execute(Map<String, Object> args);

}
