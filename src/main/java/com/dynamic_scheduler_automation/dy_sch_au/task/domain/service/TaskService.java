package com.dynamic_scheduler_automation.dy_sch_au.task.domain.service;

import com.dynamic_scheduler_automation.dy_sch_au.task.domain.model.Task;
import com.dynamic_scheduler_automation.dy_sch_au.task.domain.repository.TaskRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepositoryPort repository;

    public TaskService(TaskRepositoryPort repository) {
        this.repository = repository;
    }

    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    public Optional<Task> getTask(Long id) {
        return repository.findById(id);
    }

    public Task saveTask(Task task) {
        return repository.save(task);
    }
}
