package com.github.zjor;

import com.github.zjor.scheduler.JobDefinition;
import com.github.zjor.scheduler.JobDefinitionRepository;
import com.github.zjor.scheduler.dto.JobAction;
import com.github.zjor.scheduler.dto.JobOutput;
import com.github.zjor.scheduler.dto.JobSchedule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        var context = SpringApplication.run(Application.class, args);
        var repository = context.getBean(JobDefinitionRepository.class);
        var job = JobDefinition.builder()
                .action(JobAction.builder()
                        .type(JobAction.ActionType.CONSTANT)
                        .value("qotd")
                        .build())
                .arguments(Map.of("arg", "value", "age", 25))
                .schedule(JobSchedule.builder()
                        .type(JobSchedule.ScheduleType.CRON)
                        .value("*/3 * * * *")
                        .build())
                .output(JobOutput.builder()
                        .type(JobOutput.OutputType.HTTP)
                        .value(Map.of("url", "https://www.google.com"))
                        .build())
                .build();
        repository.save(job);
        repository.findAll().forEach(j -> {
            System.out.println(j);
        });
    }
}
