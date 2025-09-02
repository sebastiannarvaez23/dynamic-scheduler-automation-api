package com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.port;

import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.model.History;

import java.util.List;
import java.util.Optional;

public interface HistoryRepositoryPort {

    History save(History history);

    Optional<History> findById(String id);

    List<History> findAll();

}
