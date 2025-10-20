package com.dynamic_scheduler_automation.dy_sch_au.features.history.infraestructure.rest;

import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.dto.CompanyDto;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.dto.ResponseHistoryDto;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.dto.TaskDto;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
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

    @MessageMapping("/history/initial")
    @SendTo("/topic/history/initial")
    public List<ResponseHistoryDto> getInitialData() {
        try (MongoClient client = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase db = client.getDatabase("dyscau");
            MongoCollection<Document> historyCollection = db.getCollection("history");
            MongoCollection<Document> taskCollection = db.getCollection("tasks");
            MongoCollection<Document> companyCollection = db.getCollection("companies");

            List<Document> documents = new ArrayList<>();
            historyCollection.find().into(documents);

            if (documents.isEmpty()) {
                return Collections.emptyList();
            }

            documents.sort(Comparator.comparing((Document d) ->
                            d.getDate("executionDate") == null ? 0L :
                                    d.getDate("executionDate").getTime())
                    .thenComparing(d -> d.getString("executionHour") == null ? "" : d.getString("executionHour"))
                    .reversed());

            return documents.stream()
                    .limit(10)
                    .map(doc -> {
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

                        CompanyDto companyDto;
                        if (companyDoc != null) {
                            companyDto = CompanyDto.builder()
                                    .id(companyDoc.getObjectId("_id").toString())
                                    .name(companyDoc.getString("name"))
                                    .nit(companyDoc.getString("nit"))
                                    .build();
                        } else {
                            companyDto = CompanyDto.builder()
                                    .id(companyId)
                                    .name("Desconocida")
                                    .nit("")
                                    .build();
                        }

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
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
