package com.dynamic_scheduler_automation.dy_sch_au.features.task.infraestructure.config;

import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.port.TaskRepositoryPort;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.service.TaskService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskBeanConfig {

    @Bean
    public TaskService taskService(TaskRepositoryPort repositoryPort) {
        return new TaskService(repositoryPort);
    }
}