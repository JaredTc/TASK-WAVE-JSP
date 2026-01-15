package com.taskwave.taskwave.controller;

import com.taskwave.taskwave.dto.TasksResDTO;
import com.taskwave.taskwave.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TasksController.class)
class TasksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    void shouldReturnTasksWithPagination() throws Exception {

        TasksResDTO dto = new TasksResDTO();
        dto.setId(1L);
        dto.setTitle("Task test");

        Page<TasksResDTO> page =
                new PageImpl<>(List.of(dto), PageRequest.of(0, 5), 1);

        when(taskService.getAllTasks(anyString(), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/tasks/tasks_all")
                        .param("search", "test")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Task test"));
    }



}
