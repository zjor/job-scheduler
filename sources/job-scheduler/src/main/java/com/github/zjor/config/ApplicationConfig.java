package com.github.zjor.config;

import com.github.zjor.scheduler.JobDefinitionRepository;
import com.github.zjor.scheduler.JobOutputRepository;
import com.github.zjor.scheduler.SchedulerService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public SchedulerService schedulerService(
            ApplicationContext applicationContext,
            JobDefinitionRepository jobDefinitionRepository,
            JobOutputRepository jobOutputRepository) {
        return new SchedulerService(applicationContext, jobDefinitionRepository, jobOutputRepository);
    }

}
