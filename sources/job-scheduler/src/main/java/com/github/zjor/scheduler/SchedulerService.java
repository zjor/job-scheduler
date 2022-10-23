package com.github.zjor.scheduler;

import com.github.zjor.scheduler.actions.Action;
import com.github.zjor.scheduler.actions.HelloWorldAction;
import com.github.zjor.scheduler.actions.QuoteOfTheDayAction;
import com.github.zjor.scheduler.actions.WeatherAction;
import com.github.zjor.scheduler.dto.JobSchedule;
import com.github.zjor.scheduler.model.JobDefinition;
import com.github.zjor.scheduler.outputs.LoggingOutput;
import com.github.zjor.scheduler.outputs.Output;
import com.github.zjor.spring.aop.Log;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.support.CronExpression;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

@Slf4j
public class SchedulerService {

    @Getter
    private final Map<String, ScheduledFuture<?>> schedule = new ConcurrentHashMap<>();

    @Getter
    private final ScheduledExecutorService executorService;

    private final JobDefinitionRepository jobDefinitionRepository;

    private final JobOutputRepository jobOutputRepository;

    public SchedulerService(
            JobDefinitionRepository jobDefinitionRepository,
            JobOutputRepository jobOutputRepository) {
        this.jobDefinitionRepository = jobDefinitionRepository;
        this.jobOutputRepository = jobOutputRepository;

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

    private List<Output> loadOutputs(JobDefinition job) {
        return jobOutputRepository.findJobOutputsByJobOrderByCreatedAtDesc(job)
                .stream().map(o -> {
                    switch (o.getType()) {
                        case LOG:
                            return new LoggingOutput();
                        default:
                            throw new IllegalArgumentException("Unsupported type: " + o.getType());
                    }
                })
                .collect(Collectors.toList());
    }

    public void schedule(JobDefinition job) {
        try {
            var schedule = job.getSchedule();
            if (schedule.getType() != JobSchedule.ScheduleType.CRON) {
                throw new IllegalArgumentException("Unsupported schedule type: " + schedule.getType());
            }
            var cron = CronExpression.parse(schedule.getValue());
            var action = job.getAction();

            var outputs = loadOutputs(job);

            Action actionInstance;
            switch (action.getValue()) {
                case "HELLO_WORLD":
                    actionInstance = new HelloWorldAction(job.getId(), this, cron, job.getArguments(), outputs);
                    break;
                case "QOTD":
                    actionInstance = new QuoteOfTheDayAction(job.getId(), this, cron, job.getArguments(), outputs);
                    break;
                case "WEATHER":
                    actionInstance = new WeatherAction(job.getId(), this, cron, job.getArguments(), outputs);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported action: " + action.getValue());
            }
            actionInstance.scheduleNext();

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

