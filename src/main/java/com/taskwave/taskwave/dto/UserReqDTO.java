package com.taskwave.taskwave.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "User Req DTO",
        description = "Data Transfer Object for User Requests"
)
public class UserReqDTO {
    public String username;
    public String password;
    public String email;
    public String firstName;
    public String lastName;
    public String position;
    public String imgProfile;
    public Set<String> roles;
}
