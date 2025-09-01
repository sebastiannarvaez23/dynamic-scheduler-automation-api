package com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.service;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.ChangeStreamOptions;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class TaskChangeStreamService {

    private final ReactiveMongoTemplate mongoTemplate;

    @Autowired
    public TaskChangeStreamService(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Flux<ChangeStreamEvent<Document>> streamChanges() {
        return mongoTemplate.changeStream(
                "tasks",
                ChangeStreamOptions.empty(),
                Document.class
        );
    }
}
