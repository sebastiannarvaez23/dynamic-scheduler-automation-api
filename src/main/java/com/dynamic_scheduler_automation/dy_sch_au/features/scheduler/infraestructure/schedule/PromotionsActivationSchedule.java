package com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.infraestructure.schedule;

import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.domain.base.AbstractSchedule;
import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.domain.service.SimulateExecutionService;
import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.infraestructure.config.SchedulerConfig;
import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.domain.service.ExecutionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class PromotionsActivationSchedule extends AbstractSchedule {

    private static final String PROCESS = "P0011";

    private final SimulateExecutionService simulateExecutionService;

    private final String companyId;

    public PromotionsActivationSchedule(
            SchedulerConfig schedulerConfig,
            @Qualifier("taskScheduler") TaskScheduler scheduler,
            ExecutionService executionService,
            String companyId,
            SimulateExecutionService simulateExecutionService
    ) {
        super(schedulerConfig, scheduler, executionService);
        this.companyId = companyId;
        this.simulateExecutionService = simulateExecutionService;
    }

    @Override
    public String getProcess() {
        return PROCESS;
    }

    @Override
    public String getCompanyId() {
        return companyId;
    }

    @Override
    public void execute() {
        log.info("Ejecutando proceso de premios {}. Empresa: {}, "
                        + "Reejecución por procesos reversados: {}, "
                        + "Procedimientos activos a ejecutar: {}, "
                        + "Trazabilidad de la Ejecucion {}",
                getProcess(), getCompanyId(), this.execution.getStatus());
        simulateExecutionService.simulate(this.execution);
    }
}
