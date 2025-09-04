package com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.service;

import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.exceptions.TaskAlreadyExistsException;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.model.Task;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.port.TaskRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class TaskService {

    private final TaskRepositoryPort repository;

    public TaskService(TaskRepositoryPort repository) {
        this.repository = repository;
    }

    public Page<Task> getAllTasks(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<Task> getTask(String id) {
        return repository.findById(id);
    }

    public Task saveTask(Task task) {
        try {
            return repository.save(task);
        } catch (RuntimeException e) {
            throw new TaskAlreadyExistsException(task.getName());
        }
    }

}
