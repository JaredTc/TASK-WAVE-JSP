package com.taskwave.taskwave.controller;

import com.taskwave.taskwave.dto.AuthTokens;
import com.taskwave.taskwave.dto.LoginReqDTO;
import com.taskwave.taskwave.dto.LoginResDTO;
import com.taskwave.taskwave.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Authentication operations")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/login")
    @Operation(
            summary = "Login user",
            description = "Authenticate user and return access token"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<LoginResDTO> login(
            @RequestBody LoginReqDTO loginReqDTO,
            HttpServletResponse response
    ) {
        AuthTokens tokens = authService.login(loginReqDTO);

        setRefreshCookie(response, tokens.refreshToken());

        return ResponseEntity.ok(new LoginResDTO(tokens.accessToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResDTO> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String oldRefreshToken,
            HttpServletResponse response
    ) {
        System.out.println("refreshToken: " + oldRefreshToken);
        // ⚠️ Si no hay cookie, no se puede refrescar
        if (oldRefreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {

            LoginResDTO newAccess = authService.refresh(oldRefreshToken);
            String newRefreshToken = authService.generateNewRefreshToken(oldRefreshToken);
            setRefreshCookie(response, newRefreshToken);
            return ResponseEntity.ok(newAccess);

        } catch (RuntimeException e) {
            System.out.println(oldRefreshToken);
            // Si el refresh token es inválido o expiró, limpiar sesión
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Limpiar la cookie de refresh token
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // si usas https
        cookie.setPath("/"); // ruta donde se puede usar
        cookie.setMaxAge(0);  // expira inmediatamente
        response.addCookie(cookie);

        return ResponseEntity.ok("Logged out successfully");
    }

    private void setRefreshCookie(HttpServletResponse response, String refreshToken) {

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false) // ⚠️ true en producción (HTTPS)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
