package com.imatia.taskmanagerAC.exception;

/**
 * Custom exception to handle scenarios where a resource is not found.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with the specified resource ID.
     *
     * @param id the ID of the resource that was not found.
     */
    public ResourceNotFoundException(Long id) {
        super("Task with ID " + id + " not found");
    }
}
