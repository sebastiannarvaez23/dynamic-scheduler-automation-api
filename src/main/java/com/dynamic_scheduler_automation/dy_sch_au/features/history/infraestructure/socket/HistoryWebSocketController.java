package com.dynamic_scheduler_automation.dy_sch_au.features.history.infraestructure.socket;

import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.dto.CompanyDto;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.dto.ResponseHistoryDto;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.dto.TaskDto;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class HistoryWebSocketController {

    private final MongoDatabase db;

    public HistoryWebSocketController(MongoDatabase db) {
        this.db = db;
    }

    public record PageRequestDto(int page, int size) {}

    @MessageMapping("/history/get")
    @SendTo("/topic/history/initial")
    public Map<String, Object> handleGetHistories(PageRequestDto pageRequest) {
        int page = Math.max(pageRequest.page(), 0);
        int size = pageRequest.size() <= 0 ? 10 : pageRequest.size();

        Map<String, Object> response = new HashMap<>();

        try {
            MongoCollection<Document> historyCollection = db.getCollection("history");
            MongoCollection<Document> taskCollection = db.getCollection("tasks");
            MongoCollection<Document> companyCollection = db.getCollection("companies");

            List<Document> documents = new ArrayList<>();
            historyCollection.find().into(documents);

            int totalElements = documents.size();

            if (documents.isEmpty()) {
                response.put("content", Collections.emptyList());
                response.put("totalElements", 0);
                return response;
            }

            documents.sort(Comparator.comparing((Document d) ->
                            d.getDate("executionDate") == null ? 0L :
                                    d.getDate("executionDate").getTime())
                    .thenComparing(d -> d.getString("executionHour") == null ? "" : d.getString("executionHour"))
                    .reversed());

            int fromIndex = page * size;
            int toIndex = Math.min(fromIndex + size, documents.size());

            if (fromIndex >= documents.size()) {
                response.put("content", Collections.emptyList());
                response.put("totalElements", totalElements);
                return response;
            }

            List<ResponseHistoryDto> content = documents.subList(fromIndex, toIndex).stream()
                    .map(doc -> buildResponseHistory(doc, taskCollection, companyCollection))
                    .collect(Collectors.toList());

            response.put("content", content);
            response.put("totalElements", totalElements);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("content", Collections.emptyList());
            response.put("totalElements", 0);
        }

        return response;
    }

    private ResponseHistoryDto buildResponseHistory(Document doc,
                                                    MongoCollection<Document> taskCollection,
                                                    MongoCollection<Document> companyCollection) {
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
                        ? doc.getDate("executionDate").toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        : null)
                .executionHour(doc.getString("executionHour"))
                .executionTime(doc.getString("executionTime"))
                .status(doc.getString("status"))
                .build();
    }
}