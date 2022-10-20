package com.github.zjor.config;

import com.github.zjor.scheduler.JobDefinitionRepository;
import com.github.zjor.scheduler.SchedulerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public SchedulerService schedulerService(JobDefinitionRepository jobDefinitionRepository) {
        return new SchedulerService(jobDefinitionRepository);
    }

}
