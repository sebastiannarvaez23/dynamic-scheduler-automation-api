package com.dynamic_scheduler_automation.dy_sch_au.shared.dtos;

import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.domain.model.Status;

import java.time.LocalDate;

public record HistoryRecordDTO(
        String process,
        LocalDate executionDate,
        String executionHour,
        String executionTime,
        Status status,
        String companyId
) {}