package com.github.zjor.config;

import com.github.zjor.scheduler.JobDefinitionRepository;
import com.github.zjor.scheduler.JobOutputRepository;
import com.github.zjor.scheduler.SchedulerService;
import com.github.zjor.spring.logging.LoggingService;
import com.github.zjor.spring.logging.LoggingServiceImpl;
import com.github.zjor.spring.logging.RequestLoggingInterceptor;
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

    @Bean
    public LoggingService loggingService() {
        return new LoggingServiceImpl();
    }

    @Bean
    public RequestLoggingInterceptor loggingInterceptor(LoggingService loggingService) {
        return new RequestLoggingInterceptor(loggingService);
    }

}
