package com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.application.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.domain.base.AbstractSchedule;
import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.domain.service.SimulateExecutionService;
import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.infraestructure.config.SchedulerConfig;
import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.domain.factory.ScheduleFactory;
import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.infraestructure.schedule.PremiosSchedule;
import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.domain.service.ExecutionService;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

@Component
public class ScheduleRegistry {

    private final Map<String, ScheduleFactory> scheduleFactories = new HashMap<>();

    public ScheduleRegistry(
    	@Lazy SchedulerConfig schedulerConfig,
        TaskScheduler taskScheduler,
        ExecutionService executionService,
        SimulateExecutionService simulateExecutionService
    ) {
        scheduleFactories.put("P0001", empresaId ->
            new PremiosSchedule(schedulerConfig, taskScheduler, executionService, empresaId, simulateExecutionService)
        );
    }

    public Optional<AbstractSchedule> createSchedule(String procesoId, String empresaId) {
        return Optional.ofNullable(scheduleFactories.get(procesoId))
                       .map(factory -> factory.create(empresaId));
    }
}