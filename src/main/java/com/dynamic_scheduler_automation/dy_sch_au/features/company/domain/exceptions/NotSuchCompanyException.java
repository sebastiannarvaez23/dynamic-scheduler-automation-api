package com.dynamic_scheduler_automation.dy_sch_au.features.company.domain.exceptions;

public class NotSuchCompanyException extends RuntimeException {
    public NotSuchCompanyException(String id) {
        super("No se encontró tarea con id: " + id);
    }
}
