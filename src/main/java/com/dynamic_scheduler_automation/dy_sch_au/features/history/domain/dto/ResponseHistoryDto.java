package com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ResponseHistoryDto {

    private String id;

    private TaskDto task;

    private LocalDate executionDate;

    private String executionHour;

    private String executionTime;

    private String status;

}
