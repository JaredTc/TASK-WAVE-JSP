package com.taskwave.taskwave.controller;

import com.taskwave.taskwave.dto.LoginReqDTO;
import com.taskwave.taskwave.dto.LoginResDTO;
import com.taskwave.taskwave.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Athentication operations")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    @Operation(
            summary = "Login user",
            description = "Endpoint to login a user and receive a JWT token"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<LoginResDTO> login(
            @RequestBody LoginReqDTO loginReqDTO){
        return ResponseEntity.ok(authService.login(loginReqDTO));
    }
}
