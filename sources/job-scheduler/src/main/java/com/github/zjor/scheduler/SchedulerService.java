package com.github.zjor.scheduler;

import com.github.zjor.scheduler.actions.HelloWorldAction;
import com.github.zjor.scheduler.actions.QuoteOfTheDayAction;
import com.github.zjor.scheduler.dto.JobSchedule;
import com.github.zjor.spring.aop.Log;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.support.CronExpression;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

@Slf4j
public class SchedulerService {

    @Getter
    private final Map<String, ScheduledFuture<?>> schedule = new ConcurrentHashMap<>();

    @Getter
    private final ScheduledExecutorService executorService;

    private final JobDefinitionRepository jobDefinitionRepository;

    public SchedulerService(JobDefinitionRepository jobDefinitionRepository) {
        this.jobDefinitionRepository = jobDefinitionRepository;

        // TODO: thread pool
        executorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Log
    public void loadAndSchedule() {
        schedule.forEach((id, future) -> {
            future.cancel(true);
            schedule.remove(id);
        });
        jobDefinitionRepository.findAll().forEach(this::schedule);
    }

    public void schedule(JobDefinition job) {
        try {
            var schedule = job.getSchedule();
            if (schedule.getType() != JobSchedule.ScheduleType.CRON) {
                throw new IllegalArgumentException("Unsupported schedule type: " + schedule.getType());
            }
            var cron = CronExpression.parse(schedule.getValue());
            var action = job.getAction();

            switch (action.getValue()) {
                case "HELLO_WORLD":
                    (new HelloWorldAction(job.getId(), this, cron, job.getArguments())).scheduleNext();
                    break;
                case "QOTD":
                    (new QuoteOfTheDayAction(job.getId(), this, cron, job.getArguments())).scheduleNext();
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported action: " + action.getValue());
            }

            log.info("Scheduled job: {} at schedule: {}", job.getAction().getType(), schedule.getValue());
        } catch (Throwable t) {
            log.error("Failed to schedule job (ID: " + job.getId() + "): " + t.getMessage(), t);
        }
    }

    public void stop(String jobId) {
        var future = schedule.get(jobId);
        if (future != null) {
            future.cancel(true);
            schedule.remove(jobId);
        }
    }
}

