package com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class Execution {

    private LocalDate date;

    private String process;

    private LocalTime initHour;

    private LocalTime endHour;

    private Status status;

    private String companyId;

}
