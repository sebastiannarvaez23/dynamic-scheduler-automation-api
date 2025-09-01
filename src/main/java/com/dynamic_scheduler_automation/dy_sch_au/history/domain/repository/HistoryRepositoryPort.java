package com.dynamic_scheduler_automation.dy_sch_au.history.domain.repository;

import com.dynamic_scheduler_automation.dy_sch_au.history.domain.model.History;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryRepositoryPort {

    History save(History history);

    Optional<History> findById(Long id);

    List<History> findAll();

}