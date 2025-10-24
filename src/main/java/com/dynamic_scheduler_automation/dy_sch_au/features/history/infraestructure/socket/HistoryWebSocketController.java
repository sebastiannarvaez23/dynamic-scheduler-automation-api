package com.dynamic_scheduler_automation.dy_sch_au.features.history.infraestructure.socket;

import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.dto.CompanyDto;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.dto.ResponseHistoryDto;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.dto.TaskDto;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class HistoryWebSocketController {

    private final MongoDatabase db;

    public HistoryWebSocketController(MongoDatabase db) {
        this.db = db;
    }

    public record PageRequestDto(int page, int size, Map<String, Object> filters) {}

    @MessageMapping("/history/get")
    @SendTo("/topic/history/initial")
    public Map<String, Object> handleGetHistories(PageRequestDto pageRequest) {
        int page = Math.max(pageRequest.page(), 0);
        int size = pageRequest.size() <= 0 ? 10 : pageRequest.size();
        Map<String, Object> filters = pageRequest.filters() != null ? pageRequest.filters() : new HashMap<>();

        Map<String, Object> response = new HashMap<>();
        try {
            MongoCollection<Document> historyCollection = db.getCollection("history");
            MongoCollection<Document> taskCollection = db.getCollection("tasks");
            MongoCollection<Document> companyCollection = db.getCollection("companies");

            // Construcción dinámica de filtro Mongo (usando Document o Bson)
            List<Bson> bsonFilters = new ArrayList<>();

            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value == null) continue;
                String strVal = value.toString().trim();
                if (strVal.isEmpty()) continue;

                // 1) Filtro por fecha: executionDate (esperamos "yyyy-MM-dd")
                if ("executionDate".equals(key)) {
                    try {
                        LocalDate localDate = LocalDate.parse(strVal); // ISO yyyy-MM-dd

                        // SOLUCIÓN: Usar UTC para evitar problemas de zona horaria
                        ZoneId utcZone = ZoneId.of("UTC");
                        ZonedDateTime zStart = localDate.atStartOfDay(utcZone);
                        ZonedDateTime zEnd = localDate.plusDays(1).atStartOfDay(utcZone);

                        Date start = Date.from(zStart.toInstant());
                        Date end = Date.from(zEnd.toInstant());

                        bsonFilters.add(Filters.and(
                                Filters.gte("executionDate", start),
                                Filters.lt("executionDate", end)
                        ));

                        // Log para debugging
                        log.info("Filtro de fecha - Fecha solicitada: {}, Rango UTC: {} a {}",
                                localDate, zStart, zEnd);

                    } catch (Exception ex) {
                        log.warn("Formato de executionDate inválido: {}", strVal);
                    }
                    continue;
                }

                // 2) Filtros por nombre de tarea -> buscar taskId(s)
                if ("task.name".equals(key) || "taskName".equals(key)) {
                    // buscar tasks cuyo nombre coincida con regex (case-insensitive)
                    FindIterable<Document> matchingTasks = taskCollection.find(
                            new Document("name", new Document("$regex", strVal).append("$options", "i"))
                    );
                    List<String> taskIds = new ArrayList<>();
                    matchingTasks.forEach(d -> {
                        ObjectId oid = d.getObjectId("_id");
                        if (oid != null) taskIds.add(oid.toString());
                    });
                    // si además la colección de tasks usa campo "id" no ObjectId, incluirlo también
                    if (taskIds.isEmpty()) {
                        // intenta buscar por campo "id"
                        taskCollection.find(new Document("name", new Document("$regex", strVal).append("$options", "i")))
                                .forEach(d -> {
                                    if (d.getString("id") != null) taskIds.add(d.getString("id"));
                                });
                    }
                    // si no hay matches, forzar consulta vacía
                    if (taskIds.isEmpty()) {
                        // Búsqueda que no retorna nada
                        bsonFilters.add(Filters.eq("_id", "___NO_MATCH___"));
                    } else {
                        bsonFilters.add(Filters.in("taskId", taskIds));
                    }
                    continue;
                }

                // 3) Filtros por nombre de empresa -> buscar companyId(s)
                if ("company.name".equals(key) || "companyName".equals(key)) {
                    FindIterable<Document> matchingCompanies = companyCollection.find(
                            new Document("name", new Document("$regex", strVal).append("$options", "i"))
                    );
                    List<String> companyIds = new ArrayList<>();
                    matchingCompanies.forEach(d -> {
                        ObjectId oid = d.getObjectId("_id");
                        if (oid != null) companyIds.add(oid.toString());
                    });
                    if (companyIds.isEmpty()) {
                        companyCollection.find(new Document("name", new Document("$regex", strVal).append("$options", "i")))
                                .forEach(d -> {
                                    if (d.getString("id") != null) companyIds.add(d.getString("id"));
                                });
                    }
                    if (companyIds.isEmpty()) {
                        bsonFilters.add(Filters.eq("_id", "___NO_MATCH___"));
                    } else {
                        bsonFilters.add(Filters.in("companyId", companyIds));
                    }
                    continue;
                }

                // 4) Para cualquier otro campo (string) aplicamos regex (case-insensitive)
                //    Si el campo tiene puntos (nested) lo dejamos tal cual.
                bsonFilters.add(new Document(key, new Document("$regex", strVal).append("$options", "i")));
            }

            // Construir la query final combinando filtros con AND (o vacío si no hay)
            Bson finalFilter = bsonFilters.isEmpty() ? new Document() : Filters.and(bsonFilters);

            // Obtener documentos ya filtrados
            List<Document> documents = new ArrayList<>();
            historyCollection.find(finalFilter).into(documents);

            int totalElements = documents.size();

            // Orden descendente por fecha y hora
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