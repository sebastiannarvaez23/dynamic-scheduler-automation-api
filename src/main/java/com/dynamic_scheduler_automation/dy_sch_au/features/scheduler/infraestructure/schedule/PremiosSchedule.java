package com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.infraestructure.schedule;

import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.domain.base.AbstractSchedule;
import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.infraestructure.config.SchedulerConfig;
import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.domain.service.ExecutionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class PremiosSchedule extends AbstractSchedule {

    private static final String PROCESS = "P0001";
    
    private final String companyId;

    public PremiosSchedule(
        SchedulerConfig schedulerConfig,
        @Qualifier("taskScheduler") TaskScheduler scheduler,
        ExecutionService executionService,
        String companyId
    ) {
        super(schedulerConfig, scheduler, executionService);
        this.companyId = companyId;
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
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // restablece el flag de interrupción
            log.warn("La ejecución del proceso {} fue interrumpida durante el delay", getProcess());
        }

    }
}
