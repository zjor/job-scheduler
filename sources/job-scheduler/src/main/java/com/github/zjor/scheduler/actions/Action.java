package com.github.zjor.scheduler.actions;

import com.github.zjor.scheduler.SchedulerService;
import org.springframework.scheduling.support.CronExpression;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

public abstract class Action implements Runnable {

    public static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;

    private final String jobId;
    private final SchedulerService schedulerService;
    private final CronExpression cron;

    protected Action(String jobId, SchedulerService schedulerService, CronExpression cron) {
        this.jobId = jobId;
        this.schedulerService = schedulerService;
        this.cron = cron;
    }

    protected long getDelay() {
        var now = LocalDateTime.now();
        var next = cron.next(now);
        return timeDifferenceMillis(now, next);
    }

    public void scheduleNext() {
        var future = schedulerService.getExecutorService().schedule(this::execute, getDelay(), TIME_UNIT);
        schedulerService.getSchedule().put(jobId, future);
    }

    public void execute() {
        run();
        scheduleNext();
    }

    private long toEpochMillis(LocalDateTime time) {
        ZonedDateTime zdt = ZonedDateTime.of(time, ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli();
    }

    private long timeDifferenceMillis(LocalDateTime now, LocalDateTime then) {
        return toEpochMillis(then) - toEpochMillis(now);
    }

}
