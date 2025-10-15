package com.dynamic_scheduler_automation.dy_sch_au.shared.ports;

import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.model.Task;

public interface HistoryApi {

    Task getTaskById(String id);

}