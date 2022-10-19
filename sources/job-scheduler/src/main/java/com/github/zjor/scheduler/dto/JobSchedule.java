package com.github.zjor.scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobSchedule {
    public enum ScheduleType {
        CRON
    }

    private ScheduleType type;
    private String value;
}
