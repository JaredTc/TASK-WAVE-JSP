package com.taskwave.taskwave.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TasksDTO {
    private Long id;
    private String title;
    private LocalDateTime dateEnd;
    private Long assignedTo;   // ID
    private Long category;     // ID
    private String description;
    private String status;
    private Boolean isCompleted;
    private Boolean alerts;
    private Long createdBy;    // ID
    private String objective;
    private Integer percent;
}
