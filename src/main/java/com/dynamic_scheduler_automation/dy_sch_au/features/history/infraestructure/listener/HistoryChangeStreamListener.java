package com.dynamic_scheduler_automation.dy_sch_au.features.history.infraestructure.listener;

import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.dto.CompanyDto;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.dto.ResponseHistoryDto;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.dto.TaskDto;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import com.mongodb.client.model.changestream.OperationType;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.*;

@Log4j2
@Component
public class HistoryChangeStreamListener {

    private final SimpMessagingTemplate messagingTemplate;

    private final MongoDatabase db;

    private static final ZoneId COT_ZONE = ZoneId.of("America/Bogota");

    public HistoryChangeStreamListener(SimpMessagingTemplate messagingTemplate, MongoDatabase db) {
        this.messagingTemplate = messagingTemplate;
        this.db = db;
    }

    private MongoCollection<Document> historyCollection;
    private MongoCollection<Document> taskCollection;
    private MongoCollection<Document> companyCollection;

    @PostConstruct
    public void startListening() {
        historyCollection = db.getCollection("history");
        taskCollection = db.getCollection("tasks");
        companyCollection = db.getCollection("companies");

        sendInitialData();

        new Thread(() -> historyCollection.watch()
                .fullDocument(FullDocument.UPDATE_LOOKUP)
                .forEach((ChangeStreamDocument<Document> change) -> {

                    OperationType opType = change.getOperationType();
                    Document doc = change.getFullDocument();

                    if (doc == null) {
                        log.warn("⚠️ Documento de cambio nulo. Tipo: {}", opType);
                        return;
                    }

                    ResponseHistoryDto response = buildResponseFromDocument(doc);

                    if (response != null) {
                        String eventType;
                        switch (opType) {
                            case INSERT -> eventType = "INSERT";
                            case UPDATE, REPLACE -> eventType = "UPDATE";
                            case DELETE -> eventType = "DELETE";
                            default -> eventType = "UNKNOWN";
                        }

                        messagingTemplate.convertAndSend("/topic/history/change", Map.of(
                                "type", eventType,
                                "content", response
                        ));
                    }
                })).start();
    }

    private void sendInitialData() {
        List<ResponseHistoryDto> allHistories = new ArrayList<>();
        List<ResponseHistoryDto> finalAllHistories = allHistories;
        historyCollection.find().forEach(doc -> {
            ResponseHistoryDto dto = buildResponseFromDocument(doc);
            if (dto != null) finalAllHistories.add(dto);
        });

        allHistories.sort(Comparator
                .comparing((ResponseHistoryDto h) -> h.getExecutionDate() == null ? 0L : h.getExecutionDate().toEpochDay())
                .thenComparing(ResponseHistoryDto::getExecutionHour, Comparator.nullsLast(Comparator.naturalOrder()))
                .reversed());

        if (allHistories.size() > 10) allHistories = allHistories.subList(0, 10);

        log.info("📤 Enviando {} historiales iniciales al frontend (ordenados por fecha y hora).", allHistories.size());
        messagingTemplate.convertAndSend("/topic/history/initial", allHistories);
    }

    private ResponseHistoryDto buildResponseFromDocument(Document doc) {
        try {
            // --- Task ---
            String taskId = doc.getString("taskId");
            Document taskDoc = null;
            if (taskId != null) {
                try {
                    taskDoc = taskCollection.find(new Document("_id", new ObjectId(taskId))).first();
                } catch (IllegalArgumentException e) {
                    taskDoc = taskCollection.find(new Document("id", taskId)).first();
                }
            }

            TaskDto taskDto = (taskDoc != null)
                    ? TaskDto.builder()
                    .id(taskDoc.getObjectId("_id").toString())
                    .name(taskDoc.getString("name"))
                    .description(taskDoc.getString("description"))
                    .cronExpression(taskDoc.getString("cronExpression"))
                    .active(taskDoc.getBoolean("active", false))
                    .build()
                    : TaskDto.builder()
                    .id(taskId)
                    .name("Desconocido")
                    .description("")
                    .cronExpression("")
                    .active(false)
                    .build();

            // --- Company ---
            String companyId = doc.getString("companyId");
            Document companyDoc = null;
            if (companyId != null) {
                try {
                    companyDoc = companyCollection.find(new Document("_id", new ObjectId(companyId))).first();
                } catch (IllegalArgumentException e) {
                    companyDoc = companyCollection.find(new Document("id", companyId)).first();
                }
            }

            CompanyDto companyDto = (companyDoc != null)
                    ? CompanyDto.builder()
                    .id(companyDoc.getObjectId("_id").toString())
                    .name(companyDoc.getString("name"))
                    .nit(companyDoc.getString("nit"))
                    .build()
                    : CompanyDto.builder()
                    .id(companyId)
                    .name("Desconocida")
                    .nit("")
                    .build();

            return ResponseHistoryDto.builder()
                    .id(doc.getObjectId("_id").toString())
                    .task(taskDto)
                    .company(companyDto)
                    .executionDate(doc.getDate("executionDate") != null
                            ? doc.getDate("executionDate").toInstant().atZone(COT_ZONE).toLocalDate()
                            : null)
                    .executionHour(doc.getString("executionHour"))
                    .executionTime(doc.getString("executionTime"))
                    .status(doc.getString("status"))
                    .build();

        } catch (Exception e) {
            log.error("❌ Error construyendo DTO de history: {}", e.getMessage());
            return null;
        }
    }
}