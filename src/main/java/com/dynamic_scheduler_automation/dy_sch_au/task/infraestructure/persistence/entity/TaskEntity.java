package com.dynamic_scheduler_automation.dy_sch_au.task.infraestructure.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "tasks")
public class TaskEntity {

    @Id
    private String id;

    private String name;

    private String description;

    private String cronExpression;

    private boolean active;

}
