package com.dynamic_scheduler_automation.dy_sch_au.shared.dtos;

import java.time.LocalDate;

public record HistoryRecordDTO(
        String proceso,
        LocalDate executionDate,
        String executionHour,
        String executionTime,
        String status
) {}