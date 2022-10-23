package com.github.zjor.scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobAction {
    public enum ActionType {
        CONSTANT
    }

    private ActionType type;
    private String value;
}
