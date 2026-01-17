package com.taskwave.taskwave.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Response DTO for user registration")
public class RegisterResDTO {
//    private boolean success;
    private String message;
}
