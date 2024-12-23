package com.imatia.taskmanagerAC.tasks.mapper;

import com.imatia.taskmanagerAC.tasks.dto.TaskDto;
import com.imatia.taskmanagerAC.tasks.model.TaskEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskDto toDto(TaskEntity task);

    List<TaskDto> toDto(List<TaskEntity> tasks);

    TaskEntity toEntity(TaskDto taskDto);
}
