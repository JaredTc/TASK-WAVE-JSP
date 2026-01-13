package com.taskwave.taskwave.service;

import com.taskwave.taskwave.dto.TasksDTO;
import com.taskwave.taskwave.dto.TasksResDTO;
import com.taskwave.taskwave.entity.Category;
import com.taskwave.taskwave.entity.Tasks;
import com.taskwave.taskwave.entity.User;
import com.taskwave.taskwave.repository.Categoryrepository;
import com.taskwave.taskwave.repository.TaskReposirtory;
import com.taskwave.taskwave.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TaskService {

    private TaskReposirtory taskReposirtory;
    private final UserRepository userRepository;
    private final Categoryrepository categoryRepository;

    public TaskService(TaskReposirtory taskReposirtory,
                       UserRepository userRepository,
                       Categoryrepository categoryRepository
    ) {
        this.taskReposirtory = taskReposirtory;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;

    }

    public List<TasksResDTO> getAllTasks() {
        return taskReposirtory.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }
    private TasksResDTO mapToDto(Tasks task) {
        return new TasksResDTO(
                task.getId(),
                task.getTitle(),
                task.getDateEnd(),
                task.getAssignedTo().getUsername(),
                task.getCategory().getName(), // 👈 nombre
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


}
