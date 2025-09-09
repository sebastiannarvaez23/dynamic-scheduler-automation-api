package com.dynamic_scheduler_automation.dy_sch_au.features.company.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class Company {

    private String id;

    @NotBlank(message = "El nit no puede estar vacío")
    @NotNull(message = "El nit es obligatorio")
    @Size(max = 10, message = "El nit no puede superar los 10 caracteres")
    private String nit;

    @NotBlank(message = "El nombre no puede estar vacío")
    @NotNull(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede superar los 50 caracteres")
    private String name;

    @NotBlank(message = "La descripción no puede estar vacío")
    @NotNull(message = "La descripción es obligatoria")
    @Size(max = 200, message = "La descripción no puede superar los 200 caracteres")
    private String description;

    @NotNull(message = "El estado activo/inactivo es obligatorio")
    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

}
