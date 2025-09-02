package com.dynamic_scheduler_automation.dy_sch_au.features.task.infraestructure.persistence.repository;

import com.dynamic_scheduler_automation.dy_sch_au.features.task.infraestructure.persistence.entity.TaskEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends MongoRepository<TaskEntity, String> {

    TaskEntity save(TaskEntity task);

    Optional<TaskEntity> findById(String id);

    List<TaskEntity> findAll();

}
