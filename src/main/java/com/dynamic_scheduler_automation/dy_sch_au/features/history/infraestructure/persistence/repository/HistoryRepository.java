package com.dynamic_scheduler_automation.dy_sch_au.features.history.infraestructure.persistence.repository;

import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.model.History;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.repository.HistoryRepositoryPort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends MongoRepository<History, Long>, HistoryRepositoryPort {}