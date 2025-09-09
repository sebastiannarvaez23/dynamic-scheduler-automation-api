package com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.schedule.financiero;

import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.base.AbstractSchedule;
import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.config.SchedulerConfig;
import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.service.ExecutionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class PremiosSchedule extends AbstractSchedule {

    private static final String PROCESO_ID = "P0001";
    
    private final Long empresaId;

    public PremiosSchedule(
        SchedulerConfig schedulerConfig,
        @Qualifier("taskScheduler") TaskScheduler scheduler,
        ExecutionService executionService,
        Long empresaId
    ) {
        super(schedulerConfig, scheduler, executionService);
        this.empresaId = empresaId;
    }

    @Override
    public String getProcesoId() {
        return PROCESO_ID;
    }

    @Override
    public Long getEmpresaId() {
        return empresaId;
    }

    @Override
    public void execute() {
    	log.info("Ejecutando proceso de premios {}. Empresa: {}, "
    			+ "Reejecución por procesos reversados: {}, "
    			+ "Procedimientos activos a ejecutar: {}, "
    			+ "Trazabilidad de la Ejecucion {}",
    			getProcesoId(), getEmpresaId(), this.execution.getEstado());
    }
}
