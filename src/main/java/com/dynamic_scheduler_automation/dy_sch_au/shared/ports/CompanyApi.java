package com.dynamic_scheduler_automation.dy_sch_au.shared.ports;

import com.dynamic_scheduler_automation.dy_sch_au.features.company.domain.model.Company;

import java.util.Optional;

public interface CompanyApi {

    Optional<Company> getCompanyById(String id);

}
