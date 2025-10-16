package com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.domain.service;

import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.domain.model.Execution;
import com.dynamic_scheduler_automation.dy_sch_au.shared.dtos.HistoryRecordDTO;
import com.dynamic_scheduler_automation.dy_sch_au.shared.ports.HistoryApi;
import org.springframework.stereotype.Service;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;
import java.time.LocalTime;

@Log4j2
@Service
public class ExecutionService {

	private final HistoryApi historyApi;
	
	private static final String ESTADO_INICIAL = "I";
	
	private static final String ESTADO_TERMINADO = "T";

	public ExecutionService(HistoryApi historyApi) {
		this.historyApi = historyApi;
	}
	
	public Execution initExecution(String procesoId, String empresaId) {
		LocalTime now = LocalTime.now();

		Execution ejecucion = Execution.builder()
				.estado(ESTADO_INICIAL)
				.build();

		historyApi.createHistory(
				new HistoryRecordDTO(
						procesoId,
						LocalDate.now(),
						now.toString(),
						"00:00",
						"EJECUTANDO"
				)
		);
		return ejecucion;
	}
	
	public Execution completeExecution(Execution execution) {
		execution.setEstado(ESTADO_TERMINADO);
		return execution;
	}
    
}
