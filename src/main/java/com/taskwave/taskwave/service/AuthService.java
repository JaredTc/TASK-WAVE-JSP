package com.taskwave.taskwave.service;

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

@Service
public class AuthService {

    private  final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    public AuthService(AuthRepository authRepository, PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResDTO login(LoginReqDTO request) {
        try{
            User user = authRepository.findByUsername(request.getUsername())
                    .orElseThrow(UserNotFoundException::new);

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new InvalidCredentialsException();
            }

            String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getUsername());
            String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getUsername());

            return new LoginResDTO(accessToken, refreshToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

}
