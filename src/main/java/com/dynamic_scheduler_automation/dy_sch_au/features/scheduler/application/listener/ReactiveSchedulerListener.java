package com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.application.listener;

import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.domain.base.AbstractSchedule;
import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.application.registry.ScheduleRegistry;
import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.infraestructure.config.SchedulerConfig;
import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.infraestructure.listener.TaskChangeStreamService;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.model.Task;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.*;

@Slf4j
@Service
public class ReactiveSchedulerListener {

    private final TaskChangeStreamService changeStreamService;
    private final ScheduleRegistry scheduleRegistry;
    private final SchedulerConfig schedulerConfig;

    private final Map<String, AbstractSchedule> activeSchedules = new HashMap<>();

    public ReactiveSchedulerListener(
            TaskChangeStreamService changeStreamService,
            ScheduleRegistry scheduleRegistry,
            SchedulerConfig schedulerConfig
    ) {
        this.changeStreamService = changeStreamService;
        this.scheduleRegistry = scheduleRegistry;
        this.schedulerConfig = schedulerConfig;

        initListener();
    }

    private void initListener() {
        Flux<org.springframework.data.mongodb.core.ChangeStreamEvent<Document>> flux = changeStreamService.streamChanges();

        flux.subscribe(event -> {
            Document fullDoc = event.getBody();
            String operation = event.getOperationType().getValue(); // insert, update, delete
            log.info("Cambio detectado en Task: {} -> {}", operation, fullDoc);

            Task task = mapDocumentToTask(fullDoc);

            if (task.getDeletedAt() != null || "delete".equalsIgnoreCase(operation)) {
                cancelTaskSchedules(task);
            } else {
                updateTaskSchedules(task);
            }
        });
    }

    private Task mapDocumentToTask(Document doc) {
        Task task = new Task();
        // Convertimos ObjectId a String para el ID
        Object idObj = doc.get("_id");
        if (idObj instanceof ObjectId objectId) {
            task.setId(objectId.toHexString());
        } else {
            task.setId(idObj != null ? idObj.toString() : null);
        }

        task.setCode(doc.getString("code"));
        task.setName(doc.getString("name"));
        task.setDescription(doc.getString("description"));
        task.setCronExpression(doc.getString("cronExpression"));
        task.setActive(doc.getBoolean("active", false));

        // Convertimos la lista de companies correctamente
        task.setCompanies(mapCompanies(doc.get("companies")));

        return task;
    }

    private List<String> mapCompanies(Object companiesObj) {
        if (companiesObj == null) return List.of();

        List<?> rawList = (List<?>) companiesObj;

        return rawList.stream()
                .map(obj -> {
                    if (obj instanceof ObjectId objectId) {
                        return objectId.toHexString();
                    } else {
                        return obj.toString();
                    }
                })
                .toList();
    }

    private void cancelTaskSchedules(Task task) {
        if (task.getCompanies() == null) return;

        for (String companyId : task.getCompanies()) {
            String key = task.getCode() + "_" + companyId;
            AbstractSchedule schedule = activeSchedules.remove(key);
            if (schedule != null) {
                schedule.cancelarTarea();
                log.info("Schedule cancelado para {} - {}", task.getCode(), companyId);
            }
        }
    }

    private void updateTaskSchedules(Task task) {
        if (task.getCompanies() == null || !task.getActive()) return;

        for (String companyId : task.getCompanies()) {
            String key = task.getCode() + "_" + companyId;

            AbstractSchedule existing = activeSchedules.get(key);
            if (existing != null) {
                existing.cancelarTarea();
            }

            schedulerConfig.actualizarCron(task.getCode(), List.of(companyId), task.getCronExpression());

            scheduleRegistry.createSchedule(task.getCode(), companyId)
                    .ifPresent(schedule -> {
                        schedule.programarTarea();
                        activeSchedules.put(key, schedule);
                        log.info("Schedule programado para {} - {}", task.getCode(), companyId);
                    });
        }
    }
}