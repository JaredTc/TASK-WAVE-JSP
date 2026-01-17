package com.taskwave.taskwave.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for delete response")
public class DeleteResponseDTO {
    private String message;
    private Long id;
    private LocalDateTime deletedAt;


}