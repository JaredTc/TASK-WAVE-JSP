package com.taskwave.taskwave.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final String secret;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    public String getSecret() {
        return secret;
    }

    // Método privado genérico
    private String generateToken(Long userId, String username, long expirationMillis) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("username", username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, secret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    // Genera Access Token (expira rápido)
    public String generateAccessToken(Long userId, String username) {
        return generateToken(userId, username, 15 * 60 * 1000); // 15 min
    }

    // Genera Refresh Token (expira más lento)
    public String generateRefreshToken(Long userId, String username) {
        return generateToken(userId, username, 7 * 24 * 60 * 60 * 1000L); // 7 días
    }
}


