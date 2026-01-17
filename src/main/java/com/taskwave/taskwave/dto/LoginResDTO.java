package com.taskwave.taskwave.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "DTO for login response")
public class LoginResDTO {
    private String token;
    private  String refreshToken;
}
