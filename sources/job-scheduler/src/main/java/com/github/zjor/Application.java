package com.github.zjor;

import com.github.zjor.scheduler.JobDefinition;
import com.github.zjor.scheduler.JobDefinitionRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        var context = SpringApplication.run(Application.class, args);
        var repository = context.getBean(JobDefinitionRepository.class);
        var job = JobDefinition.builder()
                .action("{}")
                .arguments("{}")
                .schedule("{}")
                .output("{}")
                .build();
        repository.save(job);
    }
}
