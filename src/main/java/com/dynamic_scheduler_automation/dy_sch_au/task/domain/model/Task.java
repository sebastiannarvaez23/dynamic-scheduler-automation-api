package com.dynamic_scheduler_automation.dy_sch_au.task.domain.model;

import lombok.Data;
import org.springframework.http.HttpStatusCode;

@Data
public class Task {

    private String id;

    private String name;

    private String description;

    private String cronExpression;

    private boolean active;

}
