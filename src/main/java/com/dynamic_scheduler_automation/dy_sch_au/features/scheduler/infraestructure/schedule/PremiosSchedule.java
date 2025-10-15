package com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.infraestructure.schedule;

import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.domain.base.AbstractSchedule;
import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.infraestructure.config.SchedulerConfig;
import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.domain.service.ExecutionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class PremiosSchedule extends AbstractSchedule {

    private static final String PROCESO_ID = "P0001";
    
    private final String empresaId;

    public PremiosSchedule(
        SchedulerConfig schedulerConfig,
        @Qualifier("taskScheduler") TaskScheduler scheduler,
        ExecutionService executionService,
        String empresaId
    ) {
        super(schedulerConfig, scheduler, executionService);
        this.empresaId = empresaId;
    }

    @Override
    public String getProcesoId() {
        return PROCESO_ID;
    }

    @Override
    public String getEmpresaId() {
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
