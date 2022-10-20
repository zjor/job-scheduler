package com.github.zjor;

import com.github.zjor.scheduler.SchedulerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        var context = SpringApplication.run(Application.class, args);
        var service = context.getBean(SchedulerService.class);
        service.f();

    }
}
