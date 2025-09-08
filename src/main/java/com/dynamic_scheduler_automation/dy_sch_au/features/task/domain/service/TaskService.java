package com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.service;

import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.exceptions.TaskAlreadyExistsException;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.model.Task;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.port.TaskRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public class TaskService {

    private final TaskRepositoryPort repository;

    public TaskService(TaskRepositoryPort repository) {
        this.repository = repository;
    }

    public Page<Task> getAllTasks(
            Pageable pageable,
            String name,
            String description,
            String cronExpression,
            Boolean active
    ) {
        Pageable sortedByCreatedAt = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("createdAt").descending()
        );
        return repository.findAllWithFilters(sortedByCreatedAt, name, description, cronExpression, active);
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

    public Task updateTask(String id, Task task) {
        Optional<Task> existing = repository.findById(id);

        if (existing.isEmpty()) {
            throw new TaskAlreadyExistsException("Task with id " + id + " not found");
        }

        Task updated = new Task(
                id,
                task.getName(),
                task.getDescription(),
                task.getCronExpression(),
                task.getActive(),
                existing.get().getCreatedAt()
        );

        return repository.save(updated);
    }

}
