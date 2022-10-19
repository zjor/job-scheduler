package com.github.zjor.scheduler.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zjor.scheduler.JobDefinition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeConverter;

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
