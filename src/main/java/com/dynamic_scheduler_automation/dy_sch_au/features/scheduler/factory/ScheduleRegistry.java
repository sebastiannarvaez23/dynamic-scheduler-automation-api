package com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.base.AbstractSchedule;
import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.config.SchedulerConfig;
import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.schedule.financiero.PremiosSchedule;
import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.service.ExecutionService;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

@Component
public class ScheduleRegistry {

    private final Map<String, ScheduleFactory> scheduleFactories = new HashMap<>();

    public ScheduleRegistry(
    	@Lazy SchedulerConfig schedulerConfig,
        TaskScheduler taskScheduler,
        ExecutionService executionService
    ) {
        scheduleFactories.put("P0001", empresaId ->
            new PremiosSchedule(schedulerConfig, taskScheduler, executionService, empresaId)
        );
    }

    public Optional<AbstractSchedule> createSchedule(String procesoId, Long empresaId) {
        return Optional.ofNullable(scheduleFactories.get(procesoId))
                       .map(factory -> factory.create(empresaId));
    }
}