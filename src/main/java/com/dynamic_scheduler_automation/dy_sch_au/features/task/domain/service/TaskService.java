package com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.service;

import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.exceptions.NotSuchTaskException;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.exceptions.TaskAlreadyExistsException;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.model.Task;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.infraestructure.mapper.TaskMapper;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.infraestructure.persistence.entity.TaskEntity;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.infraestructure.persistence.repository.TaskRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository repository;

    private final TaskMapper mapper;

    public TaskService(TaskRepository repository, TaskMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<Task> getAllTasks() {
        return mapper.toDomainList(repository.findAll());
    }

    public Optional<Task> getTask(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    public Task saveTask(Task task) {
        try {
            TaskEntity entity = mapper.toEntity(task);
            TaskEntity saved = repository.save(entity);
            return mapper.toDomain(saved);
        } catch (DuplicateKeyException e) {
            throw new TaskAlreadyExistsException(task.getName());
        }
    }

}
