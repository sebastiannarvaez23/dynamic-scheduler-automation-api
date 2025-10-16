package com.dynamic_scheduler_automation.dy_sch_au.features.history.application;

import com.dynamic_scheduler_automation.dy_sch_au.features.history.infraestructure.persistence.entity.HistoryEntity;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.infraestructure.persistence.repository.HistoryRepository;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.infraestructure.persistence.entity.TaskEntity;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.infraestructure.persistence.repository.TaskRepository;
import com.dynamic_scheduler_automation.dy_sch_au.shared.dtos.HistoryRecordDTO;
import com.dynamic_scheduler_automation.dy_sch_au.shared.ports.HistoryApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HistoryAdapter implements HistoryApi {

    private final HistoryRepository repository;

    private final TaskRepository taskRepository;

    @Override
    public void createHistory(HistoryRecordDTO history) {
        String taskId = taskRepository.findByCode(history.proceso())
                .map(TaskEntity::getId)
                .orElse(null);
        HistoryEntity entity = new HistoryEntity(
                null,
                taskId,
                history.executionDate(),
                history.executionHour(),
                history.executionTime(),
                history.status()
        );
        repository.save(entity);
    }
}