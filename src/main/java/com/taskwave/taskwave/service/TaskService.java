package com.taskwave.taskwave.service;

import com.taskwave.taskwave.dto.TasksDTO;
import com.taskwave.taskwave.dto.TasksResDTO;
import com.taskwave.taskwave.entity.Category;
import com.taskwave.taskwave.entity.Tasks;
import com.taskwave.taskwave.entity.User;
import com.taskwave.taskwave.repository.Categoryrepository;
import com.taskwave.taskwave.repository.TaskReposirtory;
import com.taskwave.taskwave.repository.UserRepository;
import com.taskwave.taskwave.util.TasksMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
    private final UserRepository userRepository;
    private final Categoryrepository categoryRepository;

    public TaskService(TaskReposirtory taskReposirtory,
                       UserRepository userRepository,
                       Categoryrepository categoryRepository,
                       TasksMapper tasksMapper) {
        this.taskReposirtory = taskReposirtory;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.tasksMapper = tasksMapper;
    }

    /**
     * GET ALL TASKS REGISTERED.
     *
     * @return Lista de tareas en formato DTO
     */
    public List<TasksResDTO> getAllTasks() {
        return taskReposirtory.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    /**
     * Convierte una entidad Tasks a su DTO de respuesta.
     *
     * NOTA:
     * Este método se utiliza principalmente en creación y listado.
     *
     * @param task Entidad Tasks
     * @return DTO listo para enviar al cliente
     */
    private TasksResDTO mapToDto(Tasks task) {
        return new TasksResDTO(
                task.getId(),
                task.getTitle(),
                task.getDateEnd(),
                task.getAssignedTo().getUsername(),
                task.getCategory().getName(), // nombre
                task.getDescription(),
                task.getStatus(),
                task.getIsCompleted(),
                task.getAlerts(),
                task.getCreatedBy().getUsername(),
                task.getObjective(),
                task.getPercent(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
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
    public TasksResDTO create(TasksDTO dto) {

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

        User assigned = userRepository.findById(dto.getAssignedTo())
                .orElseThrow(() -> new RuntimeException("Usuario asignado no existe"));

        User creator = userRepository.findById(dto.getCreatedBy())
                .orElseThrow(() -> new RuntimeException("Usuario creador no existe"));

        Category category = categoryRepository.findById(dto.getCategory())
                .orElseThrow(() -> new RuntimeException("Category no existe"));

        task.setAssignedTo(assigned);
        task.setCreatedBy(creator);
        task.setCategory(category);

        return mapToDto(taskReposirtory.save(task));
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
            User assigned = userRepository.findById(dto.getAssignedTo())
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
            throw new EntityNotFoundException("Tarea no existe");
        }
        taskReposirtory.deleteById(id);
    }


}
