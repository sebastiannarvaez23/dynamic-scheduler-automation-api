package com.dynamic_scheduler_automation.dy_sch_au.features.company.application;

import com.dynamic_scheduler_automation.dy_sch_au.features.company.domain.model.Company;
import com.dynamic_scheduler_automation.dy_sch_au.features.company.domain.service.CompanyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyUseCase {

    private final CompanyService service;

    public CompanyUseCase(CompanyService service) {
        this.service = service;
    }

    public Page<Company> listCompanies(
            Pageable pageable,
            String nit,
            String name,
            String description,
            Boolean active) {
        return service.getAllCompanies(pageable, nit, name, description, active);
    }

    public Optional<Company> getCompanyById(String id) {
        return service.getCompany(id);
    }

    public Company createCompany(Company company) {
        return service.saveCompany(company);
    }

    public Company updateCompany(String id, Company company) {
        return service.updateCompany(id, company);
    }

    public void deleteCompany(String id) {
        service.deleteCompany(id);
    }

}
