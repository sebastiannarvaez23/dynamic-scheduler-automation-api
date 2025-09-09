package com.dynamic_scheduler_automation.dy_sch_au.features.company.infraestructure.persistence.repository;

import com.dynamic_scheduler_automation.dy_sch_au.features.company.infraestructure.persistence.entity.CompanyEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompanyRepository extends MongoRepository<CompanyEntity, String> {}