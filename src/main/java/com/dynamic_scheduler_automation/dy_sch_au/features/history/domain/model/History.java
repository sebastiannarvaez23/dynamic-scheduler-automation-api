package com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class History {

    private String id;

    @NotBlank(message = "La referencia a la tarea no puede estar vacía")
    @NotNull(message = "La referencia a la tarea es obligatoria")
    private String taskId;

    @NotBlank(message = "La referencia a la empresa no puede estar vacía")
    @NotNull(message = "La referencia a la empresa es obligatoria")
    private String companyId;

    @PastOrPresent(message = "La fecha de ejecución no puede ser en el futuro")
    @NotNull(message = "La fecha de ejecución es obligatoria")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate executionDate;

    @NotBlank(message = "La hora de ejecución no puede estar vacía")
    @NotNull(message = "La hora de ejecución es obligatoria")
    @Pattern(
        regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d$",
        message = "La hora de ejecución debe estar en formato HH:mm (00:00 a 23:59)"
    )
    private String executionHour;

    @NotBlank(message = "El tiempo de ejecución no puede estar vacío")
    @NotNull(message = "El tiempo de ejecución es obligatorio")
    @Pattern(
        regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d$",
        message = "La hora de ejecución debe estar en formato HH:mm (00:00 a 23:59)"
    )
    private String executionTime;

    @NotBlank(message = "El estado de la ejecución no puede estar vacío")
    @NotNull(message = "El estado de la ejecución es obligatorio")
    private String status;

}
