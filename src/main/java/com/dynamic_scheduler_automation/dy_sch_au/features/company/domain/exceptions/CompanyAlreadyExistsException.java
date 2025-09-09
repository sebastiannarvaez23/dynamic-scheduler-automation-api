package com.dynamic_scheduler_automation.dy_sch_au.features.company.domain.exceptions;

public class CompanyAlreadyExistsException extends RuntimeException {
    public CompanyAlreadyExistsException(String name) {
        super("Ya existe una tarea con el nombre: " + name);
    }
}