package com.dynamic_scheduler_automation.dy_sch_au.features.task.infraestructure.persistence.repository;

import com.dynamic_scheduler_automation.dy_sch_au.features.task.infraestructure.persistence.entity.TaskEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<TaskEntity, String> {}