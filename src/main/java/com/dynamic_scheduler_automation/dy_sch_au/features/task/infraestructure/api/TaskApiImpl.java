package com.dynamic_scheduler_automation.dy_sch_au.features.task.infraestructure.api;

import com.dynamic_scheduler_automation.dy_sch_au.features.task.application.TaskUseCase;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.model.Task;
import com.dynamic_scheduler_automation.dy_sch_au.shared.ports.TaskApi;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TaskApiImpl implements TaskApi {

    private final TaskUseCase useCase;

    public TaskApiImpl(TaskUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public Optional<Task> getTaskById(String id) {
        return useCase.getTaskById(id);
    }

}
