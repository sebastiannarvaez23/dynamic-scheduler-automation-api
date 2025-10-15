package com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.port;

import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TaskRepositoryPort {

    Page<Task> findAllWithFilters(Pageable pageable,
                                  String code,
                                  String name,
                                  String description,
                                  String cronExpression,
                                  Boolean active);

    Optional<Task> findById(String id);

    Task save(Task task);

    void softDelete(String id);

}
