package com.dynamic_scheduler_automation.dy_sch_au.features.history.infraestructure.persistence.repository;

import com.dynamic_scheduler_automation.dy_sch_au.features.history.infraestructure.persistence.entity.HistoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface HistoryRepository extends MongoRepository<HistoryEntity, String> {

    Optional<HistoryEntity> findByTaskIdAndCompanyIdAndExecutionDateAndStatus(
            String taskId,
            String companyId,
            LocalDate executionDate,
            String status
    );

}