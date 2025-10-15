package com.dynamic_scheduler_automation.dy_sch_au.features.task.infraestructure.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tasks")
public class TaskEntity {

    @Id
    private String id;

    @Indexed(unique = true)
    private String code;

    @Indexed(unique = true)
    private String name;

    private String description;

    private String cronExpression;

    private Boolean active;

    private List<String> companies;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

}
