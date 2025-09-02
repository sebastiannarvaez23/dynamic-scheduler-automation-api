package com.dynamic_scheduler_automation.dy_sch_au.features.history.infraestructure.config;

import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.exceptions.HistoryAlreadyExistsException;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.exceptions.NotSuchHistoryException;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.exceptions.NotSuchTaskException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class HistoryExceptionHandler {

    @ExceptionHandler(HistoryAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleHistoryAlreadyExists(HistoryAlreadyExistsException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(NotSuchHistoryException.class)
    public ResponseEntity<Map<String, String>> handleNotSuchHistory(NotSuchHistoryException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

}
