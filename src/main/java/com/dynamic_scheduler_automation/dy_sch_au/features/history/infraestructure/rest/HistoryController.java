package com.dynamic_scheduler_automation.dy_sch_au.features.history.infraestructure.rest;

import com.dynamic_scheduler_automation.dy_sch_au.features.history.application.HistoryUseCase;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.dto.ResponseHistoryDto;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.exceptions.NotSuchHistoryException;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.model.History;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    private final HistoryUseCase useCase;

    public HistoryController(HistoryUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping
    public List<ResponseHistoryDto> listAll() {
        return useCase.listHistories();
    }

    @GetMapping("/{id}")
    public History getById(@PathVariable String id) {
        return useCase.getHistoryById(id).orElseThrow(() -> new NotSuchHistoryException(id));
    }

    @PostMapping
    public ResponseEntity<History> create(@Valid @RequestBody History history) {
        return new ResponseEntity<>(useCase.createHistory(history), HttpStatus.CREATED);
    }

}