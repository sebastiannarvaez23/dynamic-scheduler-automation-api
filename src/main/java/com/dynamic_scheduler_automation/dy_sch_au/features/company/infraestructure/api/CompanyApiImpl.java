package com.dynamic_scheduler_automation.dy_sch_au.features.company.infraestructure.api;

import com.dynamic_scheduler_automation.dy_sch_au.features.company.application.CompanyUseCase;
import com.dynamic_scheduler_automation.dy_sch_au.features.company.domain.model.Company;
import com.dynamic_scheduler_automation.dy_sch_au.shared.ports.CompanyApi;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CompanyApiImpl implements CompanyApi {

    private final CompanyUseCase useCase;

    public CompanyApiImpl(CompanyUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public Optional<Company> getCompanyById(String id) {
        return useCase.getCompanyById(id);
    }

}
