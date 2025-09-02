package com.dynamic_scheduler_automation.dy_sch_au.features.task.infraestructure.config;

import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.exceptions.NotSuchTaskException;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.exceptions.TaskAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class TaskExceptionHandler {

    @ExceptionHandler(TaskAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleTaskAlreadyExists(TaskAlreadyExistsException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

}
