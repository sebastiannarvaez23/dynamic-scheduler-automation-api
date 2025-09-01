package com.dynamic_scheduler_automation.dy_sch_au.features.history.application;

import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.model.History;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.service.HistoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoryUseCase {

    private final HistoryService service;

    public HistoryUseCase(HistoryService service) {
        this.service = service;
    }

    public List<History> listHistories() {
        return service.getAllHistories();
    }

    public Optional<History> getHistoryById(Long id) {
        return service.getHistory(id);
    }

    public History createHistory(History history) {
        return service.saveHistory(history);
    }
}