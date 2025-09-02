package com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.service;

import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.model.History;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.infraestructure.persistence.repository.HistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoryService {

    private final HistoryRepository repository;

    public HistoryService(HistoryRepository repository) {
        this.repository = repository;
    }

    public List<History> getAllHistories() {
        return repository.findAll();
    }

    public Optional<History> getHistory(String id) {
        return repository.findById(id);
    }

    public History saveHistory(History history) {
        return repository.save(history);
    }
}
