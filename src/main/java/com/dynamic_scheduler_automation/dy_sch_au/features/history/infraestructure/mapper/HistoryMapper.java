package com.dynamic_scheduler_automation.dy_sch_au.features.history.infraestructure.mapper;

import com.dynamic_scheduler_automation.dy_sch_au.features.history.domain.model.History;
import com.dynamic_scheduler_automation.dy_sch_au.features.history.infraestructure.persistence.entity.HistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HistoryMapper {

    HistoryMapper INSTANCE = Mappers.getMapper(HistoryMapper.class);

    History toDomain(HistoryEntity historyEntity);

    HistoryEntity toEntity(History history);

    List<History> toDomainList(List<HistoryEntity> historyEntities);

}