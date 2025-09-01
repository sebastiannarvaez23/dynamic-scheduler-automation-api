package com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.repository;

import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.model.Task;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepositoryPort {

    Task save(Task task);

    Optional<Task> findById(String id);

    List<Task> findAll();

}
