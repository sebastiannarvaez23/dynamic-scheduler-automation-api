package com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskDto {

    private String id;

    private String name;

    private String description;

    private String cronExpression;

    private Boolean active;

}