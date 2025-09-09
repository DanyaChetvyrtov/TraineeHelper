package ru.ci_trainee.authms.security.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import ru.ci_trainee.authms.dto.response.JwtResponse;
import ru.ci_trainee.authms.exception.exception.InvalidTokenException;
import ru.ci_trainee.authms.model.Role;
import ru.ci_trainee.authms.model.User;
import ru.ci_trainee.authms.service.UserService;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtProperties jwtProperties;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String createAccessToken(User user) {
        Claims claims = Jwts.claims()
                .subject(user.getUsername())
                .add("id", user.getId())
                .add("roles", user.getRoles().stream().map(Role::getName).toList())
                .build();
        Instant validity = Instant.now()
                .plus(jwtProperties.getAccessDuration(), ChronoUnit.MINUTES);
        return Jwts.builder()
                .claims(claims)
                .expiration(Date.from(validity))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(UUID userId, String username) {
        Claims claims = Jwts.claims()
                .subject(username)
                .add("id", userId)
                .build();
        Instant validity = Instant.now()
                .plus(jwtProperties.getRefreshDuration(), ChronoUnit.DAYS);
        return Jwts.builder()
                .claims(claims)
                .expiration(Date.from(validity))
                .signWith(secretKey)
                .compact();
    }

    @SneakyThrows
    public JwtResponse refreshUserTokens(String refreshToken) {
        if (!isValid(refreshToken)) throw new InvalidTokenException("Refresh token is expired");

        UUID userId = getId(refreshToken);
        User user = userService.getUser(userId);

        return JwtResponse.builder()
                .id(userId)
                .username(user.getUsername())
                .accessToken(createAccessToken(user))
                .refreshToken(createRefreshToken(userId, user.getUsername()))
                .build();
    }

    public boolean isValid(String token) {
        Jws<Claims> claims = Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
        return claims.getPayload().getExpiration().after(new Date());
    }

    private UUID getId(String token) {
        return UUID.fromString(getClaims(token).get("id", String.class));
    }

    private String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(
                userDetails, "", userDetails.getAuthorities()
        );
    }
}
