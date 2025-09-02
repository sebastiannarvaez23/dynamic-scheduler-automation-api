package com.dynamic_scheduler_automation.dy_sch_au.features.history.infraestructure.rest;

import com.dynamic_scheduler_automation.dy_sch_au.features.history.application.HistoryUseCase;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.model.History;
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
    public List<History> listAll() {
        return useCase.listHistories();
    }

    @GetMapping("/{id}")
    public History getById(@PathVariable String id) {
        return useCase.getHistoryById(id).orElseThrow();
    }

    @PostMapping
    public History create(@RequestBody History history) {
        return useCase.createHistory(history);
    }
}