package com.dynamic_scheduler_automation.dy_sch_au.history.infraestructure.persistence;

import com.dynamic_scheduler_automation.dy_sch_au.history.domain.model.History;
import com.dynamic_scheduler_automation.dy_sch_au.history.domain.repository.HistoryRepositoryPort;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class HistoryRepositoryAdapter implements HistoryRepositoryPort {

    private final MongoTemplate mongoTemplate;

    public HistoryRepositoryAdapter(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public History save(History history) {
        mongoTemplate.save(history);
        return history;
    }

    @Override
    public Optional<History> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<History> findAll() {
        return List.of();
    }
}
