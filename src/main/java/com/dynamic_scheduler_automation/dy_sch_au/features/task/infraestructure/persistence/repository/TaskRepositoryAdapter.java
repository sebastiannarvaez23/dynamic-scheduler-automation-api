package com.dynamic_scheduler_automation.dy_sch_au.features.task.infraestructure.persistence.repository;

import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.model.Task;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.port.TaskRepositoryPort;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.infraestructure.mapper.TaskMapper;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.infraestructure.persistence.entity.TaskEntity;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TaskRepositoryAdapter implements TaskRepositoryPort {

    private final TaskRepository repository;

    private final TaskMapper mapper;

    private final MongoTemplate mongoTemplate;

    public TaskRepositoryAdapter(TaskRepository repository, TaskMapper mapper, MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mapper = mapper;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<Task> findAllWithFilters(Pageable pageable,
                                         String code,
                                         String name,
                                         String description,
                                         String cronExpression,
                                         Boolean active) {

        List<Criteria> criteriaList = new ArrayList<>();

        if (code != null && !code.isEmpty()) {
            criteriaList.add(Criteria.where("code").regex(code, "i"));
        }
        if (name != null && !name.isEmpty()) {
            criteriaList.add(Criteria.where("name").regex(name, "i"));
        }
        if (description != null && !description.isEmpty()) {
            criteriaList.add(Criteria.where("description").regex(description, "i"));
        }
        if (cronExpression != null && !cronExpression.isEmpty()) {
            criteriaList.add(Criteria.where("cronExpression").is(cronExpression));
        }
        if (active != null) {
            criteriaList.add(Criteria.where("active").is(active));
        }

        Criteria criteria = new Criteria();
        criteriaList.add(Criteria.where("deletedAt").is(null));
        if (!criteriaList.isEmpty()) {
            criteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));
        }

        Query query = new Query(criteria).with(pageable);

        List<TaskEntity> entities = mongoTemplate.find(query, TaskEntity.class);
        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), TaskEntity.class);

        List<Task> tasks = entities.stream()
                .map(mapper::toDomain)
                .toList();

        return new PageImpl<>(tasks, pageable, total);
    }

    @Override
    public Optional<Task> findById(String id) {
        return repository.findById(id)
                .filter(entity -> entity.getDeletedAt() == null)
                .map(mapper::toDomain);
    }

    @Override
    public Task save(Task task) {
        try {
            TaskEntity entity = mapper.toEntity(task);
            TaskEntity saved = repository.save(entity);
            return mapper.toDomain(saved);
        } catch (DuplicateKeyException e) {
            throw e;
        }
    }

    @Override
    public void softDelete(String id) {
        Optional<TaskEntity> entityOpt = repository.findById(id);

        if (entityOpt.isPresent()) {
            TaskEntity entity = entityOpt.get();
            entity.setDeletedAt(LocalDateTime.now());
            repository.save(entity);
        }
    }

}