package com.taskwave.taskwave.controller;

import com.taskwave.taskwave.dto.RegisterReqDTO;
import com.taskwave.taskwave.dto.RegisterResDTO;
import com.taskwave.taskwave.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Users", description = "User registration operations")
@RestController
@RequestMapping("/api/users")
public class RegisterController {
    private  final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResDTO> register(@RequestBody RegisterReqDTO user) {
        userService.register(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new RegisterResDTO(
                        "Usuario registrado correctamente"
                ));
    }
}
