package com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {

    private String id;

    @NotBlank(message = "El código no puede estar vacío")
    @NotNull(message = "El código es obligatorio")
    @Size(max = 5, message = "El código no puede superar los 5 caracteres")
    private String code;

    @NotBlank(message = "El nombre no puede estar vacío")
    @NotNull(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede superar los 50 caracteres")
    private String name;

    @NotBlank(message = "La descripción no puede estar vacío")
    @NotNull(message = "La descripción es obligatoria")
    @Size(max = 200, message = "La descripción no puede superar los 200 caracteres")
    private String description;

    @NotBlank(message = "La expresión CRON es obligatoria")
    @NotNull(message = "La descripción es obligatoria")
    @Pattern(
            regexp = "^([0-5]?[0-9]|\\*)\\s+([0-5]?[0-9]|\\*)\\s+([0-1]?[0-9]|2[0-3]|\\*)\\s+([1-9]|[12][0-9]|3[01]|\\*)\\s+(1[0-2]|0?[1-9]|\\*)\\s+([0-6]|\\*)$",
            message = "La expresión CRON no es válida"
    )
    private String cronExpression;

    @NotNull(message = "El estado activo/inactivo es obligatorio")
    private Boolean active;

    private List<String> companies;

    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;
}
