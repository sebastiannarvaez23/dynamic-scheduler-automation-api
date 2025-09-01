package com.dynamic_scheduler_automation.dy_sch_au.features.task.infraestructure.persistence.repository;

import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.model.Task;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.repository.TaskRepositoryPort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends MongoRepository<Task, String>, TaskRepositoryPort {}
