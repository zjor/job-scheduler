package com.github.zjor.scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobOutput {
    public enum OutputType {
        HTTP
    }

    private OutputType type;
    private Object value;
}
