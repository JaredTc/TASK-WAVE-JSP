package com.taskwave.taskwave.controller;

import com.taskwave.taskwave.dto.*;
import com.taskwave.taskwave.entity.Users;
import com.taskwave.taskwave.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Users", description = "User registration operations")
@RestController
@RequestMapping("/api/users")
public class RegisterController {
    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Register a new user",
            description = "Endpoint to register a new user in the system"
    )
    @PostMapping("/register")
    public ResponseEntity<RegisterResDTO> register(@RequestBody RegisterReqDTO user) {
        userService.register(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new RegisterResDTO(
                        "Usuario registrado correctamente"
                ));
    }

    @Operation(
            summary = "Get all users",
            description = "Retrieve the complete list of registered users"
    )
    @GetMapping("/get_users")
    public ResponseEntity<List<UserResDTO>> getUsers() {
        List<UserResDTO> users = userService.listUsers();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(users);
    }

    @Operation(
            summary = "Get current user profile",
            description = "Retrieve the profile information of the currently authenticated user"
    )
    @GetMapping("/me/")
    public ResponseEntity<UserResDTO> getMe(@AuthenticationPrincipal Users currentUser) {
        UserResDTO user = userService.profileUser(currentUser);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(user);
    }

    @Operation(
            summary = "Delete a user",
            description = "Delete a user by their ID"
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DeleteResponseDTO> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DeleteResponseDTO(
                        "Usuario eliminado correctamente",
                        id, LocalDate.now().atStartOfDay()
                ));
    }
    @Operation(
            summary = "Update a user partially",
            description = "Update only the provided fields of a user"
    )
    @PatchMapping("update/{id}")
    public ResponseEntity<ResponseDTO> update(
            @PathVariable Long id,
            @RequestBody UserReqDTO dto
    ) {
        UserService user = this.userService;
        user.updateUser(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseDTO("User updated successfully",
                        HttpStatus.CREATED.value(),
                        LocalDate.now().atStartOfDay()
                ) );
    }


}
