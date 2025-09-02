package com.dynamic_scheduler_automation.dy_sch_au.features.task.application;

import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.model.Task;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskUseCase {

    private final TaskService service;

    public TaskUseCase(TaskService service) {
        this.service = service;
    }

    public List<Task> listTasks() {
        return service.getAllTasks();
    }

    public Optional<Task> getTaskById(String id) {
        return service.getTask(id);
    }

    public Task createTask(Task task) {
        return service.saveTask(task);
    }

}
