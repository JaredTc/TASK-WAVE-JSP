package com.taskwave.taskwave.service;

import com.taskwave.taskwave.dto.LoginReqDTO;
import com.taskwave.taskwave.dto.LoginResDTO;
import com.taskwave.taskwave.entity.User;
import com.taskwave.taskwave.repository.UserRepository;
import com.taskwave.taskwave.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private  final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResDTO login(LoginReqDTO request){
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(()-> new RuntimeException("usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getUsername());

        return new LoginResDTO(accessToken, refreshToken);
    }
}
