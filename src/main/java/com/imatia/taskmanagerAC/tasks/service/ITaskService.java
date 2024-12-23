package com.imatia.taskmanagerAC.tasks.service;

import com.imatia.taskmanagerAC.tasks.dto.TaskDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ITaskService {

    Page<TaskDto> findAll(String name, Boolean completed, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Optional<TaskDto> findById(Long id);

    TaskDto createTask(TaskDto task);

    TaskDto updateTask(Long id, TaskDto task);

    TaskDto partialUpdateTask(Long id, TaskDto task);

    boolean deleteById(Long id);
}
