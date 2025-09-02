package com.dynamic_scheduler_automation.dy_sch_au.shared.ports;

import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.model.Task;

import java.util.Optional;

public interface TaskApi {

    Optional<Task> getTaskById(String id);

}
