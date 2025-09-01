package com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class History {

    private Long id;

    private String action;

    private String performedBy;

    private LocalDate createdAt;

}
