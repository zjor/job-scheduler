package com.github.zjor.scheduler;

import com.github.zjor.config.ApplicationConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Import({ApplicationConfig.class})
public class SchedulerServiceTest {

    @Autowired
    private SchedulerService service;

    @Test
    public void testScheduler() {
        service.loadAndSchedule();
    }

}