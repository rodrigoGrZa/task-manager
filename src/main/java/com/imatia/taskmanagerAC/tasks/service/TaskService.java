package com.imatia.taskmanagerAC.tasks.service;

import com.imatia.taskmanagerAC.tasks.mapper.TaskMapper;
import com.imatia.taskmanagerAC.tasks.model.TaskEntity;
import com.imatia.taskmanagerAC.tasks.repository.TaskRepository;
import com.imatia.taskmanagerAC.tasks.dto.TaskDto;
import com.imatia.taskmanagerAC.tasks.specification.TaskSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service class for managing tasks. Implements business logic for creating, updating,
 * deleting, and querying tasks, utilizing the repository and mapper layers.
 */
@Service
public class TaskService implements ITaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    /**
     * Constructor for TaskService.
     *
     * @param taskRepository the repository used for task persistence operations.
     * @param taskMapper     the mapper used for converting between TaskEntity and TaskDto.
     */
    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    /**
     * Retrieves a paginated list of tasks based on the specified filters and sorting.
     *
     * @param name      filter tasks by name (case-insensitive substring match).
     * @param completed filter tasks by completion status.
     * @param startDate filter tasks created on or after this date.
     * @param endDate   filter tasks created on or before this date.
     * @param pageable  pagination and sorting information.
     * @return a paginated list of tasks matching the filters.
     */
    @Override
    public Page<TaskDto> findAll(String name, Boolean completed, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        Specification<TaskEntity> spec = Specification.where(TaskSpecification.filterByName(name))
                .and(TaskSpecification.filterByCompleted(completed))
                .and(TaskSpecification.filterByDateRange(startDate, endDate)
                        .and(TaskSpecification.orderByCompletedAndDate()));

        Page<TaskEntity> taskPage = taskRepository.findAll(spec, pageable);
        return taskPage.map(taskMapper::toDto);
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id the ID of the task.
     * @return an Optional containing the task if found, or empty if not found.
     */
    @Override
    public Optional<TaskDto> findById(Long id) {
        Optional<TaskEntity> task = taskRepository.findById(id);
        return Optional.ofNullable(taskMapper.toDto(task.orElse(null)));
    }

    /**
     * Creates a new task.
     * Ensures default values for creation date and completion status if not provided.
     *
     * @param task the task to create.
     * @return the created task as a TaskDto.
     */
    @Override
    public TaskDto createTask(TaskDto task) {
        TaskEntity taskEntity = taskMapper.toEntity(task);

        if (taskEntity.getCreationDate() == null) {
            taskEntity.setCreationDate(LocalDateTime.now());
        }

        if (taskEntity.getCompleted() == null) {
            taskEntity.setCompleted(false);
        }

        return taskMapper.toDto(taskRepository.save(taskEntity));
    }

    /**
     * Updates an existing task with the provided data. Overwrites all fields.
     *
     * @param id   the ID of the task to update.
     * @param task the updated task data.
     * @return the updated task as a TaskDto, or null if the task does not exist.
     */
    @Override
    @Transactional
    public TaskDto updateTask(Long id, TaskDto task) {
        return updateTaskInternal(id, task, false);
    }

    /**
     * Partially updates an existing task with the provided data.
     * Only non-null fields are updated.
     *
     * @param id   the ID of the task to update.
     * @param task the task data with fields to update.
     * @return the updated task as a TaskDto, or null if the task does not exist.
     */
    @Override
    @Transactional
    public TaskDto partialUpdateTask(Long id, TaskDto task) {
        return updateTaskInternal(id, task, true);
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id the ID of the task to delete.
     * @return true if the task was deleted, false if it was not found.
     */
    @Override
    @Transactional
    public boolean deleteById(Long id) {
        Optional<TaskDto> existingTask = findById(id);
        if (existingTask.isPresent()) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Internal method for updating a task.
     * Handles both full and partial updates.
     *
     * @param id        the ID of the task to update.
     * @param task      the updated task data.
     * @param isPartial flag indicating whether the update is partial or full.
     * @return the updated task as a TaskDto, or null if the task does not exist.
     */
    private TaskDto updateTaskInternal(Long id, TaskDto task, boolean isPartial) {
        Optional<TaskDto> existingTaskOpt = findById(id);
        if (existingTaskOpt.isEmpty()) {
            return null;
        }

        TaskDto existingTask = existingTaskOpt.get();

        if (!isPartial) {
            copyTaskFields(existingTask, task);
        } else {
            partialUpdateFields(existingTask, task);
        }

        if (task.getCompleted() != null) {
            existingTask.setCompleted(task.getCompleted());
            existingTask.setEndingDate(isComplete(task.getCompleted()));
        }

        TaskEntity taskEntity = taskMapper.toEntity(existingTask);

        return taskMapper.toDto(taskRepository.save(taskEntity));
    }

    /**
     * Copies all fields from the given task into the existing task.
     *
     * @param existingTask the existing task to update.
     * @param task         the task containing updated data.
     */
    private void copyTaskFields(TaskDto existingTask, TaskDto task) {
        existingTask.setName(task.getName());
        existingTask.setText(task.getText());
        existingTask.setCreationDate(task.getCreationDate());
        existingTask.setEndingDate(task.getEndingDate());
        existingTask.setCompleted(task.getCompleted());
    }

    /**
     * Partially updates fields of an existing task with non-null fields from the given task.
     *
     * @param existingTask the existing task to update.
     * @param task         the task containing fields to update.
     */
    private void partialUpdateFields(TaskDto existingTask, TaskDto task) {
        if (task.getName() != null) {
            existingTask.setName(task.getName());
        }
        if (task.getText() != null) {
            existingTask.setText(task.getText());
        }
        if (task.getCreationDate() != null) {
            existingTask.setCreationDate(task.getCreationDate());
        }
        if (task.getEndingDate() != null) {
            existingTask.setEndingDate(task.getEndingDate());
        }
    }

    /**
     * Sets the ending date of a task based on its completion status.
     *
     * @param completed the completion status of the task.
     * @return the current date and time if completed is true, or null if false.
     */
    private LocalDateTime isComplete(boolean completed) {
        return completed ? LocalDateTime.now() : null;
    }
}
