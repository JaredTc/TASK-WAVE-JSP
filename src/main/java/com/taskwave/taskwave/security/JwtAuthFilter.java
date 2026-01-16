package com.taskwave.taskwave.security;

import com.taskwave.taskwave.entity.Register;
import com.taskwave.taskwave.repository.RegisterRepository;
import com.taskwave.taskwave.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private RegisterRepository registerRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                // 🔹 PARSEAR TOKEN CORRECTAMENTE con 0.12.x
                Claims claims = Jwts.parser()
                        .setSigningKey(Keys.hmacShaKeyFor(jwtUtil.getSecret().getBytes(StandardCharsets.UTF_8)))
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                Long userId = Long.parseLong(claims.getSubject());

                Register user = registerRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("Usuario no existe"));
//                    System.out.println("Token recibido: " + token);
//                    System.out.println("UserId extraído: " + userId);
//                    System.out.println("Usuario DB: " + user);
//                    System.out.println("Roles del usuario: " + (user != null ? user.getRoles() : "null"));
                if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    user.getRoles().stream()
                                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                                            .toList()
                            );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            } catch (Exception e) {
                SecurityContextHolder.clearContext();
//                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
