package com.taskwave.taskwave.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(description = "DTO for user registration request")
public class RegisterReqDTO {
    public String username;
    public String password;
    public String email;
    public String firstName;
    public String lastName;
    public String position;
    public String imgProfile;
    public Set<String> roles;


}
