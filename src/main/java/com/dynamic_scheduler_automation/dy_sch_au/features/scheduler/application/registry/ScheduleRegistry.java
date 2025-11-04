package com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.application.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.domain.base.AbstractSchedule;
import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.domain.service.SimulateExecutionService;
import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.infraestructure.config.SchedulerConfig;
import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.domain.factory.ScheduleFactory;
import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.infraestructure.schedule.*;
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
                new AwardsPaymentSchedule(schedulerConfig, taskScheduler, executionService, empresaId, simulateExecutionService)
        );

        scheduleFactories.put("P0002", empresaId ->
                new DailyReportsSchedule(schedulerConfig, taskScheduler, executionService, empresaId, simulateExecutionService)
        );

        scheduleFactories.put("P0002", empresaId ->
                new DailyReportsSchedule(schedulerConfig, taskScheduler, executionService, empresaId, simulateExecutionService)
        );

        scheduleFactories.put("P0003", empresaId ->
                new AccountingSyncSchedule(schedulerConfig, taskScheduler, executionService, empresaId, simulateExecutionService)
        );

        scheduleFactories.put("P0004", empresaId ->
                new CashClosingSchedule(schedulerConfig, taskScheduler, executionService, empresaId, simulateExecutionService)
        );

        scheduleFactories.put("P0005", empresaId ->
                new EmailNotificationSchedule(schedulerConfig, taskScheduler, executionService, empresaId, simulateExecutionService)
        );

        scheduleFactories.put("P0006", empresaId ->
                new SystemLogsCleanupSchedule(schedulerConfig, taskScheduler, executionService, empresaId, simulateExecutionService)
        );

        scheduleFactories.put("P0007", empresaId ->
                new ExchangeRateUpdateSchedule(schedulerConfig, taskScheduler, executionService, empresaId, simulateExecutionService)
        );

        scheduleFactories.put("P0008", empresaId ->
                new ElectronicInvoiceSchedule(schedulerConfig, taskScheduler, executionService, empresaId, simulateExecutionService)
        );

        scheduleFactories.put("P0009", empresaId ->
                new DataIntegrityCheckSchedule(schedulerConfig, taskScheduler, executionService, empresaId, simulateExecutionService)
        );

        scheduleFactories.put("P0010", empresaId ->
                new DatabaseBackupSchedule(schedulerConfig, taskScheduler, executionService, empresaId, simulateExecutionService)
        );

        scheduleFactories.put("P0011", empresaId ->
                new PromotionsActivationSchedule(schedulerConfig, taskScheduler, executionService, empresaId, simulateExecutionService)
        );

        scheduleFactories.put("P0012", empresaId ->
                new PromotionsDeactivationSchedule(schedulerConfig, taskScheduler, executionService, empresaId, simulateExecutionService)
        );

        scheduleFactories.put("P0013", empresaId ->
                new ExternalConnectionCheckSchedule(schedulerConfig, taskScheduler, executionService, empresaId, simulateExecutionService)
        );

        scheduleFactories.put("P0014", empresaId ->
                new MonthlyStatementsSchedule(schedulerConfig, taskScheduler, executionService, empresaId, simulateExecutionService)
        );

        scheduleFactories.put("P0015", empresaId ->
                new DocumentExpirationControlSchedule(schedulerConfig, taskScheduler, executionService, empresaId, simulateExecutionService)
        );
    }

    public Optional<AbstractSchedule> createSchedule(String procesoId, String empresaId) {
        return Optional.ofNullable(scheduleFactories.get(procesoId))
                       .map(factory -> factory.create(empresaId));
    }
}