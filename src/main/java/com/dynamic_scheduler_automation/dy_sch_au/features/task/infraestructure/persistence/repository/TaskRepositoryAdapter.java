package com.dynamic_scheduler_automation.dy_sch_au.features.task.infraestructure.persistence.repository;

import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.model.Task;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.port.TaskRepositoryPort;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.infraestructure.mapper.TaskMapper;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.infraestructure.persistence.entity.TaskEntity;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TaskRepositoryAdapter implements TaskRepositoryPort {

    private final TaskRepository repository;
    private final TaskMapper mapper;

    public TaskRepositoryAdapter(TaskRepository repository, TaskMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Task save(Task task) {
        try {
            TaskEntity entity = mapper.toEntity(task);
            TaskEntity saved = repository.save(entity);
            return mapper.toDomain(saved);
        } catch (DuplicateKeyException e) {
            throw e; // se captura en el servicio o handler global
        }
    }

    @Override
    public Optional<Task> findById(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Task> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }
}