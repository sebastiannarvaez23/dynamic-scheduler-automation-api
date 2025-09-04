package com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.port;

import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.model.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface HistoryRepositoryPort {

    Page<History> findAll(Pageable pageable);

    Optional<History> findById(String id);

    History save(History history);

}
