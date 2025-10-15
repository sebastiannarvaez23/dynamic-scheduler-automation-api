package com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.service;

import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.exceptions.NotSuchTaskException;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.exceptions.TaskAlreadyExistsException;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.model.Task;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.port.TaskRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public class TaskService {

    private final TaskRepositoryPort repository;

    public TaskService(TaskRepositoryPort repository) {
        this.repository = repository;
    }

    public Page<Task> getAllTasks(
            Pageable pageable,
            String code,
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
        return repository.findAllWithFilters(sortedByCreatedAt, code, name, description, cronExpression, active);
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
        Task existing = repository.findById(id)
                .orElseThrow(() -> new NotSuchTaskException(id));

        if (existing.getDeletedAt() != null) {
            throw new IllegalStateException("La tarea con " + id + " fue eliminada y no puede modificarse.");
        }

        Task updated = existing.builder()
                .id(existing.getId())
                .code(task.getCode())
                .name(task.getName())
                .description(task.getDescription())
                .cronExpression(task.getCronExpression())
                .active(task.getActive())
                .companies(task.getCompanies())
                .createdAt(existing.getCreatedAt())
                .build();

        return repository.save(updated);
    }

    public void deleteTask(String id) {
        repository.softDelete(id);
    }

}
