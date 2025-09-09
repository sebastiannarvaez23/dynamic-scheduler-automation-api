package com.dynamic_scheduler_automation.dy_sch_au.features.company.infraestructure.config;

import com.dynamic_scheduler_automation.dy_sch_au.features.company.domain.port.CompanyRepositoryPort;
import com.dynamic_scheduler_automation.dy_sch_au.features.company.domain.service.CompanyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CompanyBeanConfig {

    @Bean
    public CompanyService companyService(CompanyRepositoryPort repositoryPort) {
        return new CompanyService(repositoryPort);
    }

}