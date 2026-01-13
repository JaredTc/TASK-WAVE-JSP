package com.taskwave.taskwave.controller;

import com.taskwave.taskwave.dto.TasksDTO;
import com.taskwave.taskwave.dto.TasksResDTO;
import com.taskwave.taskwave.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/")
public class TasksController {

    private final TaskService taskService;


    public TasksController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TasksResDTO>> tasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PostMapping("/create")
    public ResponseEntity<TasksResDTO> create(
            @RequestBody TasksDTO dto
    ) {
        return ResponseEntity.ok(taskService.create(dto));
    }
}
