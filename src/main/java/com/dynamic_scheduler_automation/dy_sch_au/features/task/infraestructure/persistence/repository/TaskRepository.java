package com.dynamic_scheduler_automation.dy_sch_au.features.task.infraestructure.persistence.repository;

import com.dynamic_scheduler_automation.dy_sch_au.features.task.infraestructure.persistence.entity.TaskEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TaskRepository extends MongoRepository<TaskEntity, String> {

    Optional<TaskEntity> findByCode(String code);

}