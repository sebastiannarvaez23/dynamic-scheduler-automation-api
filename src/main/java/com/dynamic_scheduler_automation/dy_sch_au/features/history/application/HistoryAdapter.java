package com.dynamic_scheduler_automation.dy_sch_au.features.history.application;

import com.dynamic_scheduler_automation.dy_sch_au.features.history.infraestructure.persistence.entity.HistoryEntity;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.infraestructure.persistence.repository.HistoryRepository;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.infraestructure.persistence.entity.TaskEntity;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.infraestructure.persistence.repository.TaskRepository;
import com.dynamic_scheduler_automation.dy_sch_au.shared.dtos.HistoryRecordDTO;
import com.dynamic_scheduler_automation.dy_sch_au.shared.ports.HistoryApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class HistoryAdapter implements HistoryApi {

    private final HistoryRepository historyRepository;

    private final TaskRepository taskRepository;

    @Override
    public void createHistory(HistoryRecordDTO history) {
        String taskId = taskRepository.findByCode(history.process())
                .map(TaskEntity::getId)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada: " + history.process()));

        HistoryEntity entity = new HistoryEntity(
                null,
                taskId,
                history.executionDate(),
                history.executionHour(),
                history.executionTime(),
                history.status(),
                history.companyId()
        );
        historyRepository.save(entity);
        log.info("✅ Historial creado: tarea={}, empresa={}, fecha={}, hora={}",
                history.process(), history.companyId(), history.executionDate(), history.executionHour());
    }

    @Override
    public void updateHistory(HistoryRecordDTO history) {
        String taskId = taskRepository.findByCode(history.process())
                .map(TaskEntity::getId)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada: " + history.process()));

        // Buscar el registro activo del día y empresa con estado EJECUTANDO
        Optional<HistoryEntity> optional = historyRepository.findByTaskIdAndCompanyIdAndExecutionDateAndStatus(
                taskId,
                history.companyId(),
                history.executionDate(),
                "EJECUTANDO"
        );

        if (optional.isEmpty()) {
            log.warn("⚠️ No existe historial activo para actualizar (tarea={}, empresa={}, fecha={})",
                    history.process(), history.companyId(), history.executionDate());
            return;
        }

        HistoryEntity entity = optional.get();
        entity.setExecutionHour(history.executionHour());
        entity.setExecutionTime(history.executionTime());
        entity.setStatus(history.status());
        historyRepository.save(entity);

        log.info("🔄 Historial actualizado: tarea={}, empresa={}, estado={}",
                history.process(), history.companyId(), history.status());
    }
}
