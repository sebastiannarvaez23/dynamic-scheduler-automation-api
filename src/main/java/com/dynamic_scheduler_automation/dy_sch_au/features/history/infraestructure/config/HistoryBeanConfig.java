package com.dynamic_scheduler_automation.dy_sch_au.features.history.infraestructure.config;

import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.port.HistoryRepositoryPort;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.service.HistoryService;
import com.dynamic_scheduler_automation.dy_sch_au.shared.ports.CompanyApi;
import com.dynamic_scheduler_automation.dy_sch_au.shared.ports.TaskApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HistoryBeanConfig {

    @Bean
    public HistoryService historyService(HistoryRepositoryPort repositoryPort, TaskApi taskApi, CompanyApi companyApi) {
        return new HistoryService(repositoryPort, taskApi, companyApi);
    }

}
