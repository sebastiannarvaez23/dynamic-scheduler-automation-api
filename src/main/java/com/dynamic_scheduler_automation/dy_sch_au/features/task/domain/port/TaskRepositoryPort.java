package com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.port;

import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TaskRepositoryPort {

    Task save(Task task);

    Optional<Task> findById(String id);

    Page<Task> findAll(Pageable pageable);

}
