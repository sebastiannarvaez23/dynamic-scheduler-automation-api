package com.dynamic_scheduler_automation.dy_sch_au.features.history.infraestructure.listener;

import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.dto.ResponseHistoryDto;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.dto.TaskDto;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import com.mongodb.client.model.changestream.OperationType;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Log4j2
@Component
public class HistoryChangeStreamListener {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final MongoClient client = MongoClients.create("mongodb://localhost:27017");
    private final MongoDatabase db = client.getDatabase("dyscau");
    private final MongoCollection<Document> historyCollection = db.getCollection("history");
    private final MongoCollection<Document> taskCollection = db.getCollection("tasks");

    @PostConstruct
    public void startListening() {
        sendInitialData();

        new Thread(() -> {
            historyCollection.watch()
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
                                    "data", response
                            ));
                        }
                    });
        }).start();
    }

    private void sendInitialData() {
        List<ResponseHistoryDto> allHistories = new ArrayList<>();
        List<ResponseHistoryDto> finalAllHistories = allHistories;
        historyCollection.find().forEach(doc -> {
            ResponseHistoryDto dto = buildResponseFromDocument(doc);
            if (dto != null) {
                finalAllHistories.add(dto);
            }
        });

        allHistories.sort(Comparator
                .comparing((ResponseHistoryDto h) -> h.getExecutionDate() == null ? 0L : h.getExecutionDate().toEpochDay())
                .thenComparing(ResponseHistoryDto::getExecutionHour, Comparator.nullsLast(Comparator.naturalOrder()))
                .reversed());

        if (allHistories.size() > 10) {
            allHistories = allHistories.subList(0, 10);
        }

        log.info("📤 Enviando {} historiales iniciales al frontend (ordenados por fecha y hora).", allHistories.size());
        messagingTemplate.convertAndSend("/topic/history/initial", allHistories);
    }

    private ResponseHistoryDto buildResponseFromDocument(Document doc) {
        try {
            String taskId = doc.getString("taskId");
            if (taskId == null || taskId.isBlank()) {
                log.warn("⚠️ Documento de history sin taskId: {}", doc.toJson());
                return null;
            }

            Document taskDoc = null;
            try {
                taskDoc = taskCollection.find(new Document("_id", new ObjectId(taskId))).first();
            } catch (IllegalArgumentException e) {
                taskDoc = taskCollection.find(new Document("id", taskId)).first();
            }

            TaskDto taskDto;
            if (taskDoc != null) {
                taskDto = TaskDto.builder()
                        .id(taskDoc.getObjectId("_id").toString())
                        .name(taskDoc.getString("name"))
                        .description(taskDoc.getString("description"))
                        .cronExpression(taskDoc.getString("cronExpression"))
                        .active(taskDoc.getBoolean("active", false))
                        .build();
            } else {
                taskDto = TaskDto.builder()
                        .id(taskId)
                        .name("Desconocido")
                        .description("")
                        .cronExpression("")
                        .active(false)
                        .build();
            }

            return ResponseHistoryDto.builder()
                    .id(doc.getObjectId("_id").toString())
                    .task(taskDto)
                    .executionDate(doc.getDate("executionDate") != null
                            ? doc.getDate("executionDate").toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
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