package com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.domain.service;

import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.domain.model.Execution;
import org.springframework.stereotype.Service;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ExecutionService {
	
	private static final String ESTADO_INICIAL = "I";
	
	private static final String ESTADO_TERMINADO = "T";
	
	public Execution iniciarEjecucion(String procesoId, String empresaId) {
		Execution ejecucion = Execution.builder()
				.estado(ESTADO_INICIAL)
				.build();
		return ejecucion;
	}
	
	public Execution finalizarEjecucion(Execution execution) {
		execution.setEstado(ESTADO_TERMINADO);
		return execution;
	}
    
}
