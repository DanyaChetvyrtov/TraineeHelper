package ru.zuzex.practice.gatewayserver.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.zuzex.practice.gatewayserver.dto.RequestOwner;
import ru.zuzex.practice.gatewayserver.security.Role;

import javax.crypto.SecretKey;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenUtils {
    private SecretKey secretKey;
    @Value("${security.jwt.secret}")
    private String secret;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public boolean isValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public RequestOwner extractRequestOwner(String token) {
        Claims claims = getClaims(token);
        Set<Role> roles = new HashSet<>();

        try {
            var roleNames = claims.get("roles", List.class);
            for(var roleName : roleNames) {
                try {
                    roles.add(Role.valueOf(roleName.toString()));
                } catch (IllegalArgumentException e) {
                    log.warn("Unknown role: {}", roleName);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to parse roles from token: {}", e.getMessage());
        }

        return RequestOwner.builder()
                .userId(claims.get("id", String.class))
                .username(claims.getSubject())
                .roles(roles)
                .expiresAt(claims.getExpiration())
                .build();
    }
}
