package com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.base.AbstractSchedule;
import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.factory.ScheduleRegistry;
import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.service.ExecutionService;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class SchedulerConfig {
    
    private final ScheduleRegistry scheduleRegistry;
    
    private final TaskScheduler taskScheduler;
    
    private final ExecutionService executionService;

    private final Map<String, Map<Long, String>> cronMap = new ConcurrentHashMap<>();
    
    private final Map<String, Map<Long, AbstractSchedule>> tareasProgramadas = new ConcurrentHashMap<>();

	public SchedulerConfig(
		ScheduleRegistry scheduleRegistry,
		TaskScheduler taskScheduler,
        ExecutionService ejecucionService
	) {
        this.scheduleRegistry = scheduleRegistry;
        this.taskScheduler = taskScheduler;
        this.executionService = ejecucionService;
    }

    @Scheduled(fixedDelay = 60000)
    public void cargarTareasDesdeDB() {
        /*List<DetalleProceso> detalles = repository.findAll();

        for (DetalleProceso detalle : detalles) {
            String procesoId = detalle.getProcesoId();
            Long empresaId = detalle.getEmpresaId();
            String nuevoCron = detalle.getExpresionCron();
            Map<Long, String> empresaCronMap = cronMap.computeIfAbsent(procesoId, k -> new ConcurrentHashMap<>());
            String cronActual = empresaCronMap.get(empresaId);

            if (nuevoCron != null && !nuevoCron.equals(cronActual)) {
                log.info("Cambio detectado en cron para proceso={} empresa={}", procesoId, empresaId);
                log.debug("Cron nuevo: {}, Cron actual: {}", nuevoCron, cronActual);

                empresaCronMap.put(empresaId, nuevoCron);

                AbstractSchedule tareaExistente = tareasProgramadas
                    .computeIfAbsent(procesoId, k -> new ConcurrentHashMap<>())
                    .get(empresaId);

                if (tareaExistente != null) {
                    tareaExistente.cancelarTarea();
                    tareaExistente.programarTarea();
                } else {
                	scheduleRegistry.createSchedule(procesoId, empresaId).ifPresent(nuevaTarea -> {
                	    registrarTarea(nuevaTarea);
                	    nuevaTarea.programarTarea();
                	    log.info("Tarea creada dinámicamente para proceso={} empresa={}", procesoId, empresaId);
                	});
                }
            }
        }*/
    }

    public void registrarTarea(AbstractSchedule schedule) {
        String procesoId = schedule.getProcesoId();
        Long empresaId = schedule.getEmpresaId();

        tareasProgramadas
            .computeIfAbsent(procesoId, k -> new ConcurrentHashMap<>())
            .put(empresaId, schedule);
    }

    public String getCronExpresion(String procesoId, Long empresaId) {
        Map<Long, String> empresaMap = cronMap.get(procesoId);
        if (empresaMap == null) {
        	return null;
        }
        return empresaMap.get(empresaId);
    }
}
