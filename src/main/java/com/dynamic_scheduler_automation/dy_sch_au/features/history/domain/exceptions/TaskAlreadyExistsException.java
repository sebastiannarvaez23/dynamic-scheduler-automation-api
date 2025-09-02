package com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.exceptions;

public class TaskAlreadyExistsException extends RuntimeException {
    public TaskAlreadyExistsException(String name) {
        super("Ya existe una tarea con el nombre: " + name);
    }
}