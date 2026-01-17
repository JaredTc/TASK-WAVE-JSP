package com.taskwave.taskwave.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data Transfer Object for Task Response")
public class TasksResDTO {
    private Long id;
    private String title;
    private LocalDateTime dateEnd;
    private String assignedTo;
    private String category;   // 👈 NOMBRE
    private String description;
    private String status;
    private Boolean isCompleted;
    private Boolean alerts;
    private String createdBy;
    private String objective;
    private Integer percent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
