package com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyDto {

    private String id;

    private String name;

    private String nit;

    private Boolean active;

}
