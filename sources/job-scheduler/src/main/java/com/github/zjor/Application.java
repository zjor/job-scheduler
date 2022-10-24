package com.github.zjor;

import com.github.zjor.scheduler.SchedulerService;
import com.github.zjor.util.UnirestLoggingInterceptor;
import kong.unirest.Unirest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        Unirest.config()
                .interceptor(new UnirestLoggingInterceptor());

        var context = SpringApplication.run(Application.class, args);
        var service = context.getBean(SchedulerService.class);
        service.loadAndSchedule();
    }
}
