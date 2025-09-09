package com.dynamic_scheduler_automation.dy_sch_au.features.company.domain.service;

import com.dynamic_scheduler_automation.dy_sch_au.features.company.domain.exceptions.NotSuchCompanyException;
import com.dynamic_scheduler_automation.dy_sch_au.features.company.domain.exceptions.CompanyAlreadyExistsException;
import com.dynamic_scheduler_automation.dy_sch_au.features.company.domain.model.Company;
import com.dynamic_scheduler_automation.dy_sch_au.features.company.domain.port.CompanyRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public class CompanyService {

    private final CompanyRepositoryPort repository;

    public CompanyService(CompanyRepositoryPort repository) {
        this.repository = repository;
    }

    public Page<Company> getAllCompanies(
            Pageable pageable,
            String nit,
            String name,
            String description,
            Boolean active
    ) {
        Pageable sortedByCreatedAt = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("createdAt").descending()
        );
        return repository.findAllWithFilters(sortedByCreatedAt, nit, name, description, active);
    }

    public Optional<Company> getCompany(String id) {
        return repository.findById(id);
    }

    public Company saveCompany(Company company) {
        try {
            return repository.save(company);
        } catch (RuntimeException e) {
            throw new CompanyAlreadyExistsException(company.getName());
        }
    }

    public Company updateCompany(String id, Company company) {
        Company existing = repository.findById(id)
                .orElseThrow(() -> new NotSuchCompanyException(id));

        if (existing.getDeletedAt() != null) {
            throw new IllegalStateException("La empresa con " + id + " fue eliminada y no puede modificarse.");
        }

        Company updated = existing.builder()
                .id(existing.getId())
                .nit(company.getNit())
                .name(company.getName())
                .description(company.getDescription())
                .active(company.getActive())
                .createdAt(existing.getCreatedAt())
                .build();

        return repository.save(updated);
    }

    public void deleteCompany(String id) {
        repository.softDelete(id);
    }

}
