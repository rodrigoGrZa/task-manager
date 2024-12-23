package com.imatia.taskmanagerAC.tasks.controller;

import com.imatia.taskmanagerAC.exception.ResourceNotFoundException;
import com.imatia.taskmanagerAC.tasks.dto.TaskDto;
import com.imatia.taskmanagerAC.tasks.service.ITaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * REST controller for managing tasks. Provides endpoints for creating, updating,
 * deleting, and querying tasks via HTTP methods.
 */
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final ITaskService taskService;

    /**
     * Constructor for TaskController.
     *
     * @param taskService the service layer used to handle task operations.
     */
    public TaskController(ITaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Retrieves a paginated list of tasks based on optional filters.
     *
     * @param name      optional filter for tasks containing the specified name (case-insensitive).
     * @param completed optional filter for tasks with a specific completion status.
     * @param startDate optional filter for tasks created on or after this date.
     * @param endDate   optional filter for tasks created on or before this date.
     * @param page      the page number for pagination (default is 0).
     * @param size      the page size for pagination (default is 5).
     * @return a paginated list of tasks matching the filters.
     */
    @GetMapping
    public ResponseEntity<Page<TaskDto>> getAllTasks(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<TaskDto> tasks = taskService.findAll(name, completed, startDate, endDate, pageable);

        return ResponseEntity.ok(tasks);
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id the ID of the task to retrieve.
     * @return the task with the specified ID, or throws a {@link ResourceNotFoundException} if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        TaskDto task = taskService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return ResponseEntity.ok(task);
    }

    /**
     * Creates a new task.
     *
     * @param task the task to create.
     * @return the created task with a 201 Created status.
     */
    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto task) {
        TaskDto createdTask = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    /**
     * Updates an existing task with the provided data.
     * Replaces all fields of the task with the new data.
     *
     * @param id   the ID of the task to update.
     * @param task the updated task data.
     * @return the updated task, or throws a {@link ResourceNotFoundException} if the task does not exist.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id, @RequestBody TaskDto task) {
        TaskDto updatedTask = taskService.updateTask(id, task);

        if (updatedTask == null) {
            throw new ResourceNotFoundException(id);
        }

        return ResponseEntity.ok(updatedTask);
    }

    /**
     * Partially updates an existing task with the provided data.
     * Only updates fields that are not null.
     *
     * @param id   the ID of the task to update.
     * @param task the partial task data.
     * @return the updated task, or throws a {@link ResourceNotFoundException} if the task does not exist.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<TaskDto> partialUpdateTask(@PathVariable Long id, @RequestBody TaskDto task) {
        TaskDto updatedTask = taskService.partialUpdateTask(id, task);

        if (updatedTask == null) {
            throw new ResourceNotFoundException(id);
        }

        return ResponseEntity.ok(updatedTask);
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id the ID of the task to delete.
     * @throws ResourceNotFoundException if the task does not exist.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
        if (!taskService.deleteById(id)) {
            throw new ResourceNotFoundException(id);
        }
    }
}
