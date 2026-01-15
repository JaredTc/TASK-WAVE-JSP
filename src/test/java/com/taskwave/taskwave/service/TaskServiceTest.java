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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.config.Task;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskReposirtory taskReposirtory;
    @Mock
    private UserRepository userRepository;
    @Mock
    private Categoryrepository categoryrepository;
    @InjectMocks
    private TaskService taskService;
    @Mock
    private TasksMapper tasksMapper;

    @Test
    void ShpulReturnPagedTestWhenSearchTasks() {

        // Arrange
        User assigned = new User();
        assigned.setUsername("jared");

        User creator = new User();
        creator.setUsername("admin");

        Category category = new Category();
        category.setName("Hogar");

        Tasks task = new Tasks();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setStatus("Pending");
        task.setAssignedTo(assigned);
        task.setCreatedBy(creator);
        task.setCategory(category);

        Page<Tasks> page = new PageImpl<>(List.of(task));
        Pageable pageable = PageRequest.of(0, 5);

        when(taskReposirtory
                .findByTitleContainingIgnoreCaseOrStatusIgnoreCase(
                        anyString(),
                        anyString(),
                        eq(pageable)
                )
        ).thenReturn(page);

        // Act
        Page<TasksResDTO> result = taskService.getAllTasks("PENDING", pageable);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Task", result.getContent().get(0).getTitle());
        assertEquals("jared", result.getContent().get(0).getAssignedTo());
        assertEquals("Hogar", result.getContent().get(0).getCategory());
    }


    @Test
    void createTask_shouldCreateTaskSuccessfully() {

        // Arrange
        TasksDTO dto = new TasksDTO();
        dto.setTitle("Implementar módulo de tareas");
        dto.setDateEnd(LocalDateTime.now());
        dto.setAssignedTo(1L);
        dto.setDescription("Crear CRUD completo de tareas");
        dto.setCategory(3L);
        dto.setStatus("PENDING");
        dto.setIsCompleted(false);
        dto.setAlerts(true);
        dto.setCreatedBy(1L);
        dto.setObjective("Finalizar backend");
        dto.setPercent(0);

        User assigned = new User();
        assigned.setId(1L);
        assigned.setUsername("jared");

        User creator = new User();
        creator.setId(1L);
        creator.setUsername("admin");

        Category category = new Category();
        category.setId(3L);
        category.setName("Backend");

        Tasks savedTask = new Tasks();
        savedTask.setId(10L);
        savedTask.setTitle(dto.getTitle());
        savedTask.setStatus(dto.getStatus());
        savedTask.setAssignedTo(assigned);
        savedTask.setCreatedBy(creator);
        savedTask.setCategory(category);

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(assigned));
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(creator));
        when(categoryrepository.findById(3L)).thenReturn(java.util.Optional.of(category));
        when(taskReposirtory.save(any(Tasks.class))).thenReturn(savedTask);

        // Act
        TasksResDTO result = taskService.create(dto);

        // Assert
        assertNotNull(result);
        assertEquals("Implementar módulo de tareas", result.getTitle());
        assertEquals("PENDING", result.getStatus());
        assertEquals("jared", result.getAssignedTo());
        assertEquals("Backend", result.getCategory());
    }

    @Test
    void shouldUpdateTaskSuccessfully() {
        // Arrange
        Long taskId = 1L;

        TasksDTO dto = new TasksDTO();
        dto.setTitle("Nuevo título");
        dto.setAssignedTo(2L); // Asignado a otro usuario
        dto.setCategory(3L);   // Nueva categoría

        // Mock de la tarea existente
        Tasks task = new Tasks();
        task.setId(taskId);
        task.setTitle("Título viejo");

        // Mock de usuario asignado
        User assignedUser = new User();
        assignedUser.setId(2L);
        assignedUser.setUsername("jared");

        // Mock de categoría
        Category category = new Category();
        category.setId(3L);
        category.setName("Backend");

        // Mock del DTO de respuesta final
        TasksResDTO responseDto = new TasksResDTO();
        responseDto.setTitle("Nuevo título");
        responseDto.setAssignedTo("jared");
        responseDto.setCategory("Backend");

        // Mocks del repository y mapper
        when(taskReposirtory.findById(taskId)).thenReturn(Optional.of(task));
        doNothing().when(tasksMapper).updateTaskFromDto(dto, task);
        when(userRepository.findById(2L)).thenReturn(Optional.of(assignedUser));
        when(categoryrepository.findById(3L)).thenReturn(Optional.of(category));
        when(taskReposirtory.save(task)).thenReturn(task);
        when(tasksMapper.toResponseDto(task)).thenReturn(responseDto);

        // Act
        TasksResDTO result = taskService.update(taskId, dto);

        // Assert
        assertNotNull(result);
        assertEquals("Nuevo título", result.getTitle());
        assertEquals("jared", result.getAssignedTo());
        assertEquals("Backend", result.getCategory());
    }



}
