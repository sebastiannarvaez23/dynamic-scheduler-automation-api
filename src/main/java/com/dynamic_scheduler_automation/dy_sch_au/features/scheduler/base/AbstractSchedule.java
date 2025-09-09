package com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.base;

import java.util.concurrent.ScheduledFuture;

import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.config.SchedulerConfig;
import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.model.Execution;
import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.service.ExecutionService;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class AbstractSchedule implements ProgramableTask {
	
    protected final SchedulerConfig schedulerConfig;
    
    protected final TaskScheduler scheduler;
    
    protected final ExecutionService executionService;

    protected Execution execution;

    private ScheduledFuture<?> future;

    public AbstractSchedule(
	    SchedulerConfig schedulerConfig,
	    TaskScheduler scheduler,
        ExecutionService executionService
	) {
	    this.schedulerConfig = schedulerConfig;
	    this.scheduler = scheduler;
	    this.executionService = executionService;
	}

    public void programarTarea() {
        String cronExpresion = schedulerConfig.getCronExpresion(getProcesoId(), getEmpresaId());

        if (cronExpresion != null) {
            future = scheduler.schedule(() -> {
                Long empresa = getEmpresaId();
                String proceso = getProcesoId();

                try {
                	log.info("******************************************************");
                	log.info("****** INICIO EJECUCIÓN PROCESO {}, EMPRESA {} *******", proceso, empresa);
                	log.info("******************************************************");
                	this.execution = executionService.iniciarEjecucion(proceso, empresa);
                    execute();
                    executionService.finalizarEjecucion(this.execution);
                    log.info("******************************************************");
                	log.info("******** FIN EJECUCIÓN PROCESO {}, EMPRESA {} ********", proceso, empresa);
                	log.info("******************************************************");
                } catch (Exception e) {
                    log.error("Error ejecutando tarea {} para empresa {}", proceso, empresa, e);
                }

            }, new CronTrigger(cronExpresion));
            log.info("Tarea programada para proceso {} y empresa {} con cron {}",
            		getProcesoId(), getEmpresaId(), cronExpresion
            );
        } else {
            log.warn("No se encontró expresión cron para proceso {} y empresa {}. No se programó la tarea.",
            		getProcesoId(), getEmpresaId());
        }
    }

    public void cancelarTarea() {
        if (future != null && !future.isCancelled()) {
            future.cancel(true);
            log.info("Tarea cancelada para proceso {} y empresa {}",
            		getProcesoId(), getEmpresaId());
        }
    }
    
    public abstract String getProcesoId();

    public abstract Long getEmpresaId();

    @Override
    public abstract void execute();
    
}

