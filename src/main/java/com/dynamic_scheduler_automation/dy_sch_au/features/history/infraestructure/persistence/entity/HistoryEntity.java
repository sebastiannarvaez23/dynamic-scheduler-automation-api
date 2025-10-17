package com.dynamic_scheduler_automation.dy_sch_au.features.history.infraestructure.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "history")
public class HistoryEntity {

    @Id
    private String id;

    private String taskId;

    private LocalDate executionDate;

    private String executionHour;

    private String executionTime;

    private String status;

    private String companyId;

}