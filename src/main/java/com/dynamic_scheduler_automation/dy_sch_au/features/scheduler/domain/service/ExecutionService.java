package com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.domain.service;

import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.domain.model.Execution;
import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.domain.model.Status;
import com.dynamic_scheduler_automation.dy_sch_au.shared.dtos.HistoryRecordDTO;
import com.dynamic_scheduler_automation.dy_sch_au.shared.ports.HistoryApi;
import org.springframework.stereotype.Service;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Log4j2
@Service
public class ExecutionService {

	private final HistoryApi historyApi;
	
	private static final Status INIT_STATUS = Status.RUNNING;
	
	private static final Status COMPLETE_STATUS = Status.COMPLETED;

	public ExecutionService(HistoryApi historyApi) {
		this.historyApi = historyApi;
	}
	
	public Execution initExecution(String process, String companyId) {
		LocalTime now = LocalTime.now();
		Execution execution = Execution.builder()
				.process(process)
				.companyId(companyId)
				.status(INIT_STATUS)
				.initHour(now)
				.date(LocalDate.now())
				.build();

		historyApi.createHistory(
				new HistoryRecordDTO(
						process,
						LocalDate.now(),
						now.toString(),
						"00:00",
						execution.getStatus(),
						companyId
				)
		);
		return execution;
	}

	public Execution completeExecution(Execution execution) {
		LocalTime endTime = LocalTime.now();
		execution.setEndHour(endTime);
		String duration = "00:00";
		if (execution.getInitHour() != null) {
			Duration time = Duration.between(execution.getInitHour(), endTime);
			long horas = time.toHours();
			long minutos = time.toMinutesPart();
			long segundos = time.toSecondsPart();
			duration = String.format("%02d:%02d:%02d", horas, minutos, segundos);
		}

		historyApi.updateHistory(
				new HistoryRecordDTO(
						execution.getProcess(),
						execution.getDate() != null ? execution.getDate() : LocalDate.now(),
						endTime.toString(),
						duration,
						execution.getStatus(),
						execution.getCompanyId()
				)
		);

		log.info("✅ Ejecución completada para proceso {}. Duración: {}", execution.getProcess(), duration);
		return execution;
	}
    
}
