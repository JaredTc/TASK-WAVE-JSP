package com.taskwave.taskwave.util;

import com.taskwave.taskwave.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class JwtUtil {

    @Getter
    private final String secret;
    private final SecretKey key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // ================= TOKEN GENERATION =================
    private String generateToken(
            Long userId,
            String username,
            Set<Role> roles,
            long expirationTime
    ) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("userId", String.valueOf(userId));
        claims.put("username", username);
        claims.put(
                "roles",
                roles.stream()
                        .map(Role::getName)
                        .toList()
        );

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    private String generateRefreshTokenInternal(
            Long userId,
            String username,
            long expirationTime
    ) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("userId", userId);
        claims.put("type", "refresh"); // opcional pero PRO

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

//    private String generateToken(Long userId, String username, long expirationMillis) {
//        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + expirationMillis);
//
//        return Jwts.builder()
//                .setSubject(String.valueOf(userId))
//                .claim("username", username)
//                .setIssuedAt(now)
//                .setExpiration(expiryDate)
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//    }

    public String generateAccessToken(Long userId, String username,  Set<Role> roles) {
//        return generateToken(userId, username,roles ,10 * 1000);
        return generateToken(userId, username, roles, 15 * 60 * 1000);
    }

    public String generateRefreshToken(Long userId, String username) {
        return generateRefreshTokenInternal(userId, username, 7 * 24 * 60 * 60 * 1000L);
//        return generateToken(userId, username, 10 * 1000);
    }

    // ================= TOKEN VALIDATION =================
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractClaims(token);
            Date exp = claims.getExpiration();
            return exp.after(new Date()); // ✅ true si no ha expirado
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Long extractUserId(String token) {
        return Long.valueOf(extractClaims(token).getSubject());
    }



    public String extractUsername(String token) {
        return extractClaims(token).get("username", String.class);
    }
}
