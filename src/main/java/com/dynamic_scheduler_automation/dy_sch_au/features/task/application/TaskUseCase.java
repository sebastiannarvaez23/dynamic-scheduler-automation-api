package com.dynamic_scheduler_automation.dy_sch_au.features.task.application;

import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.model.Task;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskUseCase {

    private final TaskService service;

    public TaskUseCase(TaskService service) {
        this.service = service;
    }

    public Page<Task> listTasks(
            Pageable pageable,
            String name,
            String description,
            String cronExpression,
            Boolean active) {
        return service.getAllTasks(pageable, name, description, cronExpression, active);
    }

    public Optional<Task> getTaskById(String id) {
        return service.getTask(id);
    }

    public Task createTask(Task task) {
        return service.saveTask(task);
    }

    public Task updateTask(String id, Task task) {
        return service.updateTask(id, task);
    }

}
