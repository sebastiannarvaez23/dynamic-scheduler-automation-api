package com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.exceptions;

public class NotSuchTaskException extends RuntimeException {
    public NotSuchTaskException(String id) {
        super("No se encontró tarea con id: " + id);
    }
}
