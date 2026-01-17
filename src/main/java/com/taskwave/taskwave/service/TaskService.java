package com.taskwave.taskwave.service;

import com.taskwave.taskwave.dto.TasksDTO;
import com.taskwave.taskwave.dto.TasksResDTO;
import com.taskwave.taskwave.entity.Category;
import com.taskwave.taskwave.entity.Users;
import com.taskwave.taskwave.entity.Tasks;
import com.taskwave.taskwave.entity.User;

import com.taskwave.taskwave.exception.ResourceNotFoundException;
import com.taskwave.taskwave.repository.Categoryrepository;
import com.taskwave.taskwave.repository.TaskReposirtory;
import com.taskwave.taskwave.repository.AuthRepository;
import com.taskwave.taskwave.util.Helper;
import com.taskwave.taskwave.util.TasksMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * Servicio encargado de la lógica de negocio relacionada con las tareas.
 *
 * Responsabilidades:
 * - Crear tareas
 * - Obtener listado de tareas
 * - Actualizar tareas (PATCH)
 *
 * Reglas de negocio:
 * - Las relaciones (User, Category) se resuelven siempre mediante repositorios
 * - No se permite crear o asignar tareas a entidades inexistentes
 *
 */

@Service
public class TaskService {



    private final TasksMapper tasksMapper;
    private TaskReposirtory taskReposirtory;
    private final AuthRepository authRepository;
    private final Categoryrepository categoryRepository;
    private final Helper helper;

    public TaskService(TaskReposirtory taskReposirtory,
                       AuthRepository authRepository,
                       Categoryrepository categoryRepository,
                       TasksMapper tasksMapper, Helper helper) {
        this.taskReposirtory = taskReposirtory;
        this.authRepository = authRepository;
        this.categoryRepository = categoryRepository;
        this.tasksMapper = tasksMapper;
        this.helper = helper;
    }

    /**
     * GET ALL TASKS REGISTERED.
     *
     * @return Lista de tareas en formato DTO
     */
    public List<TasksResDTO> getAllTasks() {
        return taskReposirtory.findAll()
                .stream()
                .map(helper::mapToDtoTask)
                .toList();
    }
    public Page<TasksResDTO> getAllTasks(String search, Pageable pageable) {

        Page<Tasks> page;
        if (search != null && !search.trim().isEmpty()) {
            page = taskReposirtory.findByTitleContainingIgnoreCaseOrStatusIgnoreCase(
                    search, search, pageable
            );
        } else {
            page = taskReposirtory.findAll(pageable);
        }
        return page.map(helper::mapToDtoTask);
    }

    public List<TasksResDTO> getTasksByUser(Users currentUser) {
        User user = authRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Tasks> tasks = taskReposirtory.findByAssignedTo(user);

        return tasks.stream()
                .map(helper::mapToDtoTask)
                .toList();
    }


    /**
     * Crea una nueva tarea.
     *
     * Validaciones:
     * - La categoría es obligatoria
     * - El usuario asignado debe existir
     * - El usuario creador debe existir
     *
     * @param dto DTO con la información de la tarea
     * @return Tarea creada en formato DTO
     */
    public TasksResDTO create(TasksDTO dto, Users currentUser) {

        if (dto.getCategory() == null || dto.getCategory() <= 0)
            throw new IllegalArgumentException("Category inválida");

        Tasks task = new Tasks();

        task.setTitle(dto.getTitle());
        task.setDateEnd(dto.getDateEnd());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setIsCompleted(dto.getIsCompleted());
        task.setAlerts(dto.getAlerts());
        task.setObjective(dto.getObjective());
        task.setPercent(dto.getPercent());

        User assigned = authRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Usuario asignado no existe"));

        User creator = authRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Usuario creador no existe"));

        Category category = categoryRepository.findById(dto.getCategory())
                .orElseThrow(() -> new RuntimeException("Category no existe"));

        task.setAssignedTo(assigned);
        task.setCreatedBy(creator);
        task.setCategory(category);

        return helper.mapToDtoTask(taskReposirtory.save(task));
    }

    /**
     * Actualiza parcialmente una tarea existente.
     *
     * Comportamiento tipo PATCH:
     * - Solo se actualizan los campos enviados en el DTO
     * - Los valores null NO sobrescriben datos existentes
     * - Las relaciones se resuelven manualmente
     *
     * @param id  ID de la tarea a actualizar
     * @param dto DTO con los campos a modificar
     * @return Tarea actualizada en formato DTO
     */
    @Transactional
    public TasksResDTO update(Long id, TasksDTO dto) {

        Tasks task = taskReposirtory.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarea no existe"));

        // Actualiza solo campos NO null
        tasksMapper.updateTaskFromDto(dto, task);

        if (dto.getAssignedTo() != null) {
            User assigned = authRepository.findById(dto.getAssignedTo())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario asignado no existe"));
            task.setAssignedTo(assigned);
        }

        if (dto.getCategory() != null) {
            Category category = categoryRepository.findById(dto.getCategory())
                    .orElseThrow(() -> new EntityNotFoundException("Categoría no existe"));
            task.setCategory(category);
        }

        return tasksMapper.toResponseDto(taskReposirtory.save(task));
    }


    /**
     * Elimina una tarea por su ID.
     *
     * @param id ID de la tarea a eliminar
     */
    @Transactional
    public void deleteTask(Long id) {
        if (!taskReposirtory.existsById(id)) {
            throw new ResourceNotFoundException("La tarea con id " + id + " no existe");
        }
        taskReposirtory.deleteById(id);
    }


}
