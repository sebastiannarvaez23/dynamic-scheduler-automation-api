package com.dynamic_scheduler_automation.dy_sch_au.features.history.infraestructure.persistence.repository;

import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.model.History;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.port.HistoryRepositoryPort;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.infraestructure.mapper.HistoryMapper;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.infraestructure.persistence.entity.HistoryEntity;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class HistoryRepositoryAdapter implements HistoryRepositoryPort {

    private final HistoryRepository repository;

    private final HistoryMapper mapper;

    public HistoryRepositoryAdapter(HistoryRepository repository, HistoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Page<History> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public Optional<History> findById(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public History save(History history) {
        try {
            HistoryEntity entity = mapper.toEntity(history);
            HistoryEntity saved = repository.save(entity);
            return mapper.toDomain(saved);
        } catch (DuplicateKeyException e) {
            throw e;
        }
    }

}
