package com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.exceptions;

public class HistoryAlreadyExistsException extends RuntimeException {
    public HistoryAlreadyExistsException(String name) {
        super("Ya existe un historico con el nombre: " + name);
    }
}
