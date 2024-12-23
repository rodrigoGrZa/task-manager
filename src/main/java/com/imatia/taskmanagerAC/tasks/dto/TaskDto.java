package com.imatia.taskmanagerAC.tasks.dto;

import java.time.LocalDateTime;

public class TaskDto {

    private Long id;

    private String name;

    private String text;

    private LocalDateTime creationDate;

    private LocalDateTime endingDate;

    private Boolean completed;

    public TaskDto(Long id, String name, String text, LocalDateTime creationDate, LocalDateTime endingDate, Boolean completed) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.creationDate = creationDate;
        this.endingDate = endingDate;
        this.completed = completed;
    }

    public TaskDto() {

    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(LocalDateTime endingDate) {
        this.endingDate = endingDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
