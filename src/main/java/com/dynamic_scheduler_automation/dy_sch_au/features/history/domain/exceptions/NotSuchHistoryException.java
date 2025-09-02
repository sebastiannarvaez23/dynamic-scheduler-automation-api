package com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.exceptions;

public class NotSuchHistoryException extends RuntimeException {
    public NotSuchHistoryException(String id) {
        super("No se encontró historico con id: " + id);
    }
}
