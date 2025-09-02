package com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.port;

import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepositoryPort {

    Task save(Task task);

    Optional<Task> findById(String id);

    List<Task> findAll();

}
