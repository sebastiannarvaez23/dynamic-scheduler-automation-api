package com.dynamic_scheduler_automation.dy_sch_au.features.company.infraestructure.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "companies")
public class CompanyEntity {

    @Id
    private String id;

    @Indexed(unique = true)
    private String nit;

    @Indexed(unique = true)
    private String name;

    private String description;

    private Boolean active;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

}
