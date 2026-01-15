package com.taskwave.taskwave.controller;

import com.taskwave.taskwave.dto.DeleteResponseDTO;
import com.taskwave.taskwave.dto.TasksDTO;
import com.taskwave.taskwave.dto.TasksResDTO;
import com.taskwave.taskwave.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
/**
 * Controlador REST para la gestión de tareas.
 *
 * Expone endpoints para:
 * - Listar tareas
 * - Crear nuevas tareas
 * - Actualizar tareas parcialmente (PATCH)
 * - Eliminar tareas
 *
 * Ruta base:
 * /api/tasks
 */
@RestController
@RequestMapping("/api/tasks/")
public class TasksController {

    private final TaskService taskService;


    public TasksController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Obtiene el listado completo de tareas.
     *
     * Método HTTP: GET
     * Endpoint: /api/tasks/tasks
     *
     * @return Lista de tareas en formato DTO
     */
    @GetMapping("/tasks")
    public ResponseEntity<List<TasksResDTO>> tasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    /**
     * Crea una nueva tarea.
     *
     * Método HTTP: POST
     * Endpoint: /api/tasks/create
     *
     * @param dto DTO con la información de la nueva tarea
     * @return Tarea creada
     */
    @PostMapping("/create")
    public ResponseEntity<TasksResDTO> create(
            @RequestBody TasksDTO dto
    ) {
        return ResponseEntity.ok(taskService.create(dto));
    }
    /**
     * Actualiza parcialmente y elimina una tarea existente.
     *
     * Comportamiento tipo PATCH:
     * - Solo se actualizan los campos enviados en el body
     * - Los valores null no sobrescriben datos existentes
     *
     * Método HTTP: PATCH
     * Endpoint: /api/tasks/update/{id}
     *
     * @param id  ID de la tarea a actualizar
     * @param dto DTO con los campos a modificar
     * @return Tarea actualizada
     */
    @PatchMapping("update/{id}")
    public ResponseEntity<TasksResDTO> update(
            @PathVariable Long id,
            @RequestBody TasksDTO dto
    ) {
        return ResponseEntity.ok(taskService.update(id, dto));
    }

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
