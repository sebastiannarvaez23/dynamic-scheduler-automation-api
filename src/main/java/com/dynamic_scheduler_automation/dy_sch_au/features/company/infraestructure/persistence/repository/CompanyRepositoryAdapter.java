package com.dynamic_scheduler_automation.dy_sch_au.features.company.infraestructure.persistence.repository;

import com.dynamic_scheduler_automation.dy_sch_au.features.company.domain.model.Company;
import com.dynamic_scheduler_automation.dy_sch_au.features.company.domain.port.CompanyRepositoryPort;
import com.dynamic_scheduler_automation.dy_sch_au.features.company.infraestructure.mapper.CompanyMapper;
import com.dynamic_scheduler_automation.dy_sch_au.features.company.infraestructure.persistence.entity.CompanyEntity;
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
public class CompanyRepositoryAdapter implements CompanyRepositoryPort {

    private final CompanyRepository repository;

    private final CompanyMapper mapper;

    private final MongoTemplate mongoTemplate;

    public CompanyRepositoryAdapter(CompanyRepository repository, CompanyMapper mapper, MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mapper = mapper;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<Company> findAllWithFilters(Pageable pageable,
                                            String nit,
                                            String name,
                                            String description,
                                            Boolean active) {

        List<Criteria> criteriaList = new ArrayList<>();

        if (nit != null && !nit.isEmpty()) {
            criteriaList.add(Criteria.where("nit").regex(nit, "i"));
        }
        if (name != null && !name.isEmpty()) {
            criteriaList.add(Criteria.where("name").regex(name, "i"));
        }
        if (description != null && !description.isEmpty()) {
            criteriaList.add(Criteria.where("description").regex(description, "i"));
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

        List<CompanyEntity> entities = mongoTemplate.find(query, CompanyEntity.class);
        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), CompanyEntity.class);

        List<Company> companies = entities.stream()
                .map(mapper::toDomain)
                .toList();

        return new PageImpl<>(companies, pageable, total);
    }

    @Override
    public Optional<Company> findById(String id) {
        return repository.findById(id)
                .filter(entity -> entity.getDeletedAt() == null)
                .map(mapper::toDomain);
    }

    @Override
    public Company save(Company company) {
        try {
            CompanyEntity entity = mapper.toEntity(company);
            CompanyEntity saved = repository.save(entity);
            return mapper.toDomain(saved);
        } catch (DuplicateKeyException e) {
            throw e;
        }
    }

    @Override
    public void softDelete(String id) {
        Optional<CompanyEntity> entityOpt = repository.findById(id);

        if (entityOpt.isPresent()) {
            CompanyEntity entity = entityOpt.get();
            entity.setDeletedAt(LocalDateTime.now());
            repository.save(entity);
        }
    }

}