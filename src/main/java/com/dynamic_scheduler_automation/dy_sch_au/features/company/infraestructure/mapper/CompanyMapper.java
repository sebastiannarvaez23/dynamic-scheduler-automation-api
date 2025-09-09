package com.dynamic_scheduler_automation.dy_sch_au.features.company.infraestructure.mapper;

import com.dynamic_scheduler_automation.dy_sch_au.features.company.domain.model.Company;
import com.dynamic_scheduler_automation.dy_sch_au.features.company.infraestructure.persistence.entity.CompanyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

    Company toDomain(CompanyEntity companyEntity);

    CompanyEntity toEntity(Company company);

    List<Company> toDomainList(List<CompanyEntity> companyEntities);

}
