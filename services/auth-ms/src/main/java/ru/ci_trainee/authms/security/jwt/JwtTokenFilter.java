package ru.ci_trainee.authms.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer "))
            bearerToken = bearerToken.substring(7);

        try {
            if (bearerToken != null && jwtTokenProvider.isValid(bearerToken)) {
                Authentication auth = jwtTokenProvider.getAuthentication(bearerToken);
                if (auth != null)
                    SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (ExpiredJwtException expiredTokenException) {
            logger.warn("Expired JWT token for user: {}", expiredTokenException.getClaims().getSubject());
//            throw new ExpiredTokenException();
        } catch (Exception ignored) {
            logger.error(ignored.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }
}