package com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.service;

import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.dto.ResponseHistoryDto;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.dto.TaskDto;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.model.History;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.port.HistoryRepositoryPort;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.exceptions.NotSuchTaskException;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.model.Task;
import com.dynamic_scheduler_automation.dy_sch_au.shared.ports.TaskApi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HistoryService {

    private final HistoryRepositoryPort repository;

    private final TaskApi taskApi;

    public HistoryService(HistoryRepositoryPort repository, TaskApi taskApi) {
        this.repository = repository;
        this.taskApi = taskApi;
    }

    public Page<ResponseHistoryDto> getAllHistories(Pageable pageable) {
        Page<History> historyPage = repository.findAll(pageable);

        return historyPage.map(history -> {
            Optional<Task> optionalTask = taskApi.getTaskById(history.getTaskId());

            TaskDto taskDto = optionalTask.map(task ->
                    TaskDto.builder()
                            .id(task.getId())
                            .name(task.getName())
                            .description(task.getDescription())
                            .cronExpression(task.getCronExpression())
                            .active(task.getActive())
                            .build()
            ).orElse(null);

            return ResponseHistoryDto.builder()
                    .id(history.getId())
                    .task(taskDto)
                    .executionDate(history.getExecutionDate())
                    .executionHour(history.getExecutionHour())
                    .executionTime(history.getExecutionTime())
                    .status(history.getStatus())
                    .build();
        });
    }

    public Optional<History> getHistory(String id) {
        return repository.findById(id);
    }

    @Transactional
    public History saveHistory(History history) {
        taskApi.getTaskById(history.getTaskId())
                .orElseThrow(() -> new NotSuchTaskException(history.getTaskId()));
        return repository.save(history);
    }

}
