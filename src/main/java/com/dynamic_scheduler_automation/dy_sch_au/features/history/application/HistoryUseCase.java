package com.dynamic_scheduler_automation.dy_sch_au.features.history.application;

import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.dto.ResponseHistoryDto;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.model.History;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.service.HistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HistoryUseCase {

    private final HistoryService service;

    public HistoryUseCase(HistoryService service) {
        this.service = service;
    }

    public Page<ResponseHistoryDto> listHistories(Pageable pageable) {
        return service.getAllHistories(pageable);
    }

    public Optional<History> getHistoryById(String id) {
        return service.getHistory(id);
    }

    public History createHistory(History history) {
        return service.saveHistory(history);
    }

}