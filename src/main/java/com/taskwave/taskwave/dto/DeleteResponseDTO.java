package com.taskwave.taskwave.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteResponseDTO {
    private String message;
    private Long id;
    private LocalDateTime deletedAt;


}