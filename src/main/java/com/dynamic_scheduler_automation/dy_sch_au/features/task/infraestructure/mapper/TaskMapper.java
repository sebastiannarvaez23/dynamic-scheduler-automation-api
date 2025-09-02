package com.dynamic_scheduler_automation.dy_sch_au.features.task.infraestructure.mapper;

import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.model.Task;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.infraestructure.persistence.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    Task toDomain(TaskEntity taskEntity);

    TaskEntity toEntity(Task task);

    List<Task> toDomainList(List<TaskEntity> taskEntities);

}
