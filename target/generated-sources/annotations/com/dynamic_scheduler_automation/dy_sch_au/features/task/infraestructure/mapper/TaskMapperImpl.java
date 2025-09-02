package com.dynamic_scheduler_automation.dy_sch_au.features.task.infraestructure.mapper;

import com.dynamic_scheduler_automation.dy_sch_au.features.task.domain.model.Task;
import com.dynamic_scheduler_automation.dy_sch_au.features.task.infraestructure.persistence.entity.TaskEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-01T20:48:57-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public Task toDomain(TaskEntity taskEntity) {
        if ( taskEntity == null ) {
            return null;
        }

        Task task = new Task();

        task.setId( taskEntity.getId() );
        task.setName( taskEntity.getName() );
        task.setDescription( taskEntity.getDescription() );
        task.setCronExpression( taskEntity.getCronExpression() );
        task.setActive( taskEntity.getActive() );

        return task;
    }

    @Override
    public TaskEntity toEntity(Task task) {
        if ( task == null ) {
            return null;
        }

        TaskEntity taskEntity = new TaskEntity();

        taskEntity.setId( task.getId() );
        taskEntity.setName( task.getName() );
        taskEntity.setDescription( task.getDescription() );
        taskEntity.setCronExpression( task.getCronExpression() );
        taskEntity.setActive( task.getActive() );

        return taskEntity;
    }

    @Override
    public List<Task> toDomainList(List<TaskEntity> taskEntities) {
        if ( taskEntities == null ) {
            return null;
        }

        List<Task> list = new ArrayList<Task>( taskEntities.size() );
        for ( TaskEntity taskEntity : taskEntities ) {
            list.add( toDomain( taskEntity ) );
        }

        return list;
    }
}
