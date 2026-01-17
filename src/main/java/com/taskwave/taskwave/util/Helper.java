package com.taskwave.taskwave.util;
import com.taskwave.taskwave.dto.TasksResDTO;
import com.taskwave.taskwave.dto.UserReqDTO;
import com.taskwave.taskwave.dto.UserResDTO;
import com.taskwave.taskwave.entity.Tasks;
import com.taskwave.taskwave.entity.Users;
import com.taskwave.taskwave.entity.Role;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
@Component
public class Helper {

    /**
     * Convert the entity to DTO Response DTo
     *
     * NOTA:
     * This  method is used for lists .
     *
     * @param task Entity of task
     * @return DTO Ready for send  to client
     */
    public TasksResDTO mapToDtoTask(Tasks task) {
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
    public UserResDTO mapToDto(Users users) {
        return  new UserResDTO(
                users.getId(),
                users.getUsername(),
                users.getEmail(),
                users.getFirstName(),
                users.getLastName(),
                users.getIs_superuser(),
                users.getIs_staff(),
                users.getIs_active(),
                users.getPosition(),
                users.getImgProfile(),
                users.getDateJoined(),
                users.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
        );

    }
}
