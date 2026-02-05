package com.taskwave.taskwave.service;

import com.taskwave.taskwave.dto.AuthTokens;
import com.taskwave.taskwave.dto.LoginReqDTO;
import com.taskwave.taskwave.dto.LoginResDTO;
import com.taskwave.taskwave.entity.User;
import com.taskwave.taskwave.exception.InvalidCredentialsException;
import com.taskwave.taskwave.exception.UserNotFoundException;
import com.taskwave.taskwave.repository.AuthRepository;
import com.taskwave.taskwave.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    private  final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    private final Set<String> validRefreshTokens = new HashSet<>();

    public AuthService(AuthRepository authRepository, PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthTokens login(LoginReqDTO request) {

        User user = authRepository.findByUsername(request.getUsername())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getUsername() , user.getRoles());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getUsername());

        return new AuthTokens(accessToken, refreshToken);
    }


    public LoginResDTO refresh(String refreshToken) {

        if (!jwtUtil.isTokenValid(refreshToken)) {
            throw new RuntimeException("Refresh token inválido");
        }

        Long userId = jwtUtil.extractUserId(refreshToken);

        User user = authRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        String newAccessToken = jwtUtil.generateRefreshToken(
                user.getId(),
                user.getUsername()
        );

        return new LoginResDTO(newAccessToken);
    }

    public String generateNewRefreshToken(String oldRefreshToken) {
        Long userId = jwtUtil.extractUserId(oldRefreshToken);
        String username = jwtUtil.extractUsername(oldRefreshToken);
        return jwtUtil.generateRefreshToken(userId, username);
    }

}
