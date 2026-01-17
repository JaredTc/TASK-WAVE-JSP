package com.taskwave.taskwave.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO for login request")
public class LoginReqDTO {
    private String username;
    private String password;
}
