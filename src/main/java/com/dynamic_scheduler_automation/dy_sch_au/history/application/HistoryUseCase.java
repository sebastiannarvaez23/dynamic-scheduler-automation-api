package com.dynamic_scheduler_automation.dy_sch_au.history.application;

import com.dynamic_scheduler_automation.dy_sch_au.history.domain.model.History;
import com.dynamic_scheduler_automation.dy_sch_au.history.domain.service.HistoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoryUseCase {

    private final HistoryService historyService;

    public HistoryUseCase(HistoryService historyService) {
        this.historyService = historyService;
    }

    public History createHistory(History history) {
        return historyService.saveHistory(history);
    }

    public Optional<History> getHistoryById(Long id) {
        return historyService.getHistory(id);
    }

    public List<History> listHistories() {
        return historyService.getAllHistories();
    }
}