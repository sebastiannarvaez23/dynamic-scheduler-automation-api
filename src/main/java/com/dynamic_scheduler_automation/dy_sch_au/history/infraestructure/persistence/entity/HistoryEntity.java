package com.dynamic_scheduler_automation.dy_sch_au.history.infraestructure.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;

@Document(collection = "history")
public class HistoryEntity {

    @Id
    private Long id;

    private Long task;

    private LocalDate executionDate;

    private String executionHour;

    private String executionTime;

    private String status;

}