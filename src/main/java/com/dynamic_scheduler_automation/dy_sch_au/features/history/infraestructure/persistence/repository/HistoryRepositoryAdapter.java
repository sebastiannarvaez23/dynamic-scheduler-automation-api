package com.dynamic_scheduler_automation.dy_sch_au.features.history.infraestructure.persistence.repository;

import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.model.History;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.port.HistoryRepositoryPort;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.infraestructure.mapper.HistoryMapper;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.infraestructure.persistence.entity.HistoryEntity;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class HistoryRepositoryAdapter implements HistoryRepositoryPort {

    private final HistoryRepository repository;

    private final HistoryMapper mapper;

    private final MongoTemplate mongoTemplate;

    public HistoryRepositoryAdapter(HistoryRepository repository, HistoryMapper mapper, MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mapper = mapper;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<History> findAllWithFilters(Pageable pageable,
                                            String taskId,
                                            String status,
                                            String executionDate) {

        List<Criteria> criteriaList = new ArrayList<>();

        if (taskId != null && !taskId.isEmpty()) {
            criteriaList.add(Criteria.where("taskId").is(taskId));
        }
        if (status != null && !status.isEmpty()) {
            criteriaList.add(Criteria.where("status").regex(status, "i"));
        }
        if (executionDate != null && !executionDate.isEmpty()) {
            criteriaList.add(Criteria.where("executionDate").is(executionDate));
        }

        Criteria criteria = new Criteria();
        if (!criteriaList.isEmpty()) {
            criteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));
        }

        Query query = new Query(criteria).with(pageable);

        List<HistoryEntity> entities = mongoTemplate.find(query, HistoryEntity.class);
        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), HistoryEntity.class);

        List<History> histories = entities.stream()
                .map(mapper::toDomain)
                .toList();

        return new PageImpl<>(histories, pageable, total);
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
