package com.dynamic_scheduler_automation.dy_sch_au.features.company.domain.port;

import com.dynamic_scheduler_automation.dy_sch_au.features.company.domain.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CompanyRepositoryPort {

    Page<Company> findAllWithFilters(Pageable pageable,
                                     String nit,
                                     String name,
                                     String description,
                                     Boolean active);

    Optional<Company> findById(String id);

    Company save(Company company);

    void softDelete(String id);

}
