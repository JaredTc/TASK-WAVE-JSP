package com.taskwave.taskwave.controller.auth;

import com.taskwave.taskwave.dto.LoginReqDTO;
import com.taskwave.taskwave.dto.LoginResDTO;
import com.taskwave.taskwave.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<LoginResDTO> login(
            @RequestBody LoginReqDTO loginReqDTO){
        return ResponseEntity.ok(authService.login(loginReqDTO));
    }
}
