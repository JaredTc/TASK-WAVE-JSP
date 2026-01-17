package com.taskwave.taskwave.controller;

import com.taskwave.taskwave.dto.DeleteResponseDTO;
import com.taskwave.taskwave.dto.ResponseDTO;
import com.taskwave.taskwave.dto.TasksDTO;
import com.taskwave.taskwave.dto.TasksResDTO;
import com.taskwave.taskwave.entity.Register;
import com.taskwave.taskwave.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 *  * Controlador REST para la gestión de tareas.
 *  * <p>
 *  * Expone endpoints para:
 *  * - Listar tareas
 *  * - Crear nuevas tareas
 *  * - Actualizar tareas parcialmente (PATCH)
 *  * - Eliminar tareas
 *  * <p>
 *  * Ruta base:
 *  * /api/tasks
 *  */
@Tag(name = "Tasks", description = "Gestion of tasks")
@RestController
@RequestMapping("/api/tasks/")
public class TasksController {

    private final TaskService taskService;


    public TasksController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Obtiene el listado completo de tareas.
     * SIN PAGINACIÓN Y CON PAGINACIÓN
     * <p>
     * Método HTTP: GET
     * Endpoint: /api/tasks/tasks
     *
     * @return Lista de tareas en formato DTO
     */
    @Operation(
            summary = "Get all tasks",
            description = "Retrieve the complete list of tasks without pagination"
    )
    @GetMapping("/tasks")
    public ResponseEntity<List<TasksResDTO>> tasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @Operation(
            summary = "Get tasks by user",
            description = "Retrieve tasks associated with the authenticated user"
    )
    @GetMapping("/tasks_by_user")
    public ResponseEntity<List<TasksResDTO>> tasksByUser(@AuthenticationPrincipal Register currentUser) {
        List<TasksResDTO> tasks = taskService.getTasksByUser(currentUser);
        return ResponseEntity.ok(tasks);
    }

    @Operation(
            summary = "Get all tasks with pagination and search",
            description = "Retrieve all tasks with pagination support and optional search filtering"
    )
    @GetMapping("/tasks_all")
    public ResponseEntity<Page<TasksResDTO>> getAllTasks(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(taskService.getAllTasks(search, pageable));

    }

    /**
     * Crea una nueva tarea.
     * <p>
     * Método HTTP: POST
     * Endpoint: /api/tasks/create
     *
     * @param dto DTO con la información de la nueva tarea
     * @return Tarea creada
     */
    @Operation(
            summary = "Create a new task",
            description = "Create a new task with the provided details"
    )
    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createTask(@RequestBody TasksDTO dto,
                                                  @AuthenticationPrincipal Register currentUser) {
        TasksResDTO task = taskService.create(dto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseDTO("Task created successfully", HttpStatus.CREATED.value(), LocalDateTime.now()) );
    }

    /**
     * Actualiza parcialmente y elimina una tarea existente.
     * <p>
     * Comportamiento tipo PATCH:
     * - Solo se actualizan los campos enviados en el body
     * - Los valores null no sobrescriben datos existentes
     * <p>
     * Método HTTP: PATCH
     * Endpoint: /api/tasks/update/{id}
     *
     * @param id  ID de la tarea a actualizar
     * @param dto DTO con los campos a modificar
     * @return Tarea actualizada
     */
    @Operation (
            summary = "Update a task partially",
            description = "Update only the provided fields of a task"
    )
    @PatchMapping("update/{id}")
    public ResponseEntity<TasksResDTO> update(
            @PathVariable Long id,
            @RequestBody TasksDTO dto
    ) {
        return ResponseEntity.ok(taskService.update(id, dto));
    }

    @Operation(
            summary = "Delete a task",
            description = "Delete a task by its ID"
    )
    @DeleteMapping("delete/{id}")
    public ResponseEntity<DeleteResponseDTO> delete(
            @PathVariable Long id
    ) {
        taskService.deleteTask(id);
        return ResponseEntity.ok(
                new DeleteResponseDTO(
                        "Tarea eliminada correctamente",
                        id,
                        LocalDateTime.now()
                )
        );
    }


}
