package com.imatia.taskmanagerAC.tasks.controller;

import com.imatia.taskmanagerAC.tasks.dto.TaskDto;
import com.imatia.taskmanagerAC.tasks.service.ITaskService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ITaskService taskService;

    @Test
    void getAllTasks_ReturnsTasks() throws Exception {
        Mockito.when(taskService.findAll(any(), any(), any(), any(), any()))
                .thenReturn(Page.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tasks")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    void getTaskById_ReturnsTask_WhenTaskExists() throws Exception {
        TaskDto mockTask = new TaskDto();
        mockTask.setId(1L);
        mockTask.setName("Test Task");

        Mockito.when(taskService.findById(1L)).thenReturn(Optional.of(mockTask));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tasks/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Task")));
    }

    @Test
    void getTaskById_ThrowsException_WhenTaskNotFound() throws Exception {
        Mockito.when(taskService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tasks/999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTask_ReturnsCreatedTask() throws Exception {
        TaskDto mockTask = new TaskDto();
        mockTask.setId(1L);
        mockTask.setName("New Task");

        Mockito.when(taskService.createTask(any(TaskDto.class))).thenReturn(mockTask);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"New Task\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("New Task")));
    }

    @Test
    void updateTask_ReturnsUpdatedTask_WhenTaskExists() throws Exception {
        TaskDto updatedTask = new TaskDto();
        updatedTask.setId(1L);
        updatedTask.setName("Updated Task");

        Mockito.when(taskService.updateTask(eq(1L), any(TaskDto.class))).thenReturn(updatedTask);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Task\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Updated Task")));
    }

    @Test
    void updateTask_ThrowsException_WhenTaskNotFound() throws Exception {
        Mockito.when(taskService.updateTask(eq(999L), any(TaskDto.class))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/tasks/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Nonexistent Task\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void partialUpdateTask_ReturnsUpdatedTask_WhenTaskExists() throws Exception {
        TaskDto updatedTask = new TaskDto();
        updatedTask.setId(1L);
        updatedTask.setName("Partially Updated Task");

        Mockito.when(taskService.partialUpdateTask(eq(1L), any(TaskDto.class))).thenReturn(updatedTask);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Partially Updated Task\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Partially Updated Task")));
    }

    @Test
    void partialUpdateTask_ThrowsException_WhenTaskNotFound() throws Exception {
        Mockito.when(taskService.partialUpdateTask(eq(999L), any(TaskDto.class))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/tasks/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Nonexistent Task\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTask_ReturnsNoContent_WhenTaskDeleted() throws Exception {
        Mockito.when(taskService.deleteById(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/tasks/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTask_ThrowsException_WhenTaskNotFound() throws Exception {
        Mockito.when(taskService.deleteById(999L)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/tasks/999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

