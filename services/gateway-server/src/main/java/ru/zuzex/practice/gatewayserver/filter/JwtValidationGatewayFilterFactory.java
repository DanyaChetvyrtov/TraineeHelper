package ru.zuzex.practice.gatewayserver.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.zuzex.practice.gatewayserver.dto.RequestOwner;
import ru.zuzex.practice.gatewayserver.security.Role;
import ru.zuzex.practice.gatewayserver.security.jwt.JwtTokenUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            try {
                String token = extractAndValidateToken(exchange);
                if (token == null)
                    return unauthorized(exchange, "Missing or invalid token format");

                if (!jwtTokenUtils.isValid(token))
                    return unauthorized(exchange, "Invalid token");

                RequestOwner requestOwner = jwtTokenUtils.extractRequestOwner(token);
                ServerHttpRequest modifiedRequest = enhanceRequestWithUserInfo(exchange.getRequest(), requestOwner);

                return chain.filter(exchange.mutate().request(modifiedRequest).build());

            } catch (Exception e) {
                log.error("Authentication error: {}", e.getMessage());
                return unauthorized(exchange, "Authentication failed");
            }

        };
    }

    private String extractAndValidateToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;

        return authHeader.substring(7);
    }

    private ServerHttpRequest enhanceRequestWithUserInfo(ServerHttpRequest request, RequestOwner userInfo) {
        return request.mutate()
                .header("X-User-Id", userInfo.getUserId())
                .header("X-User-Name", userInfo.getUsername())
                .header("X-User-Roles", rolesToString(userInfo.getRoles()))
                .build();
    }

    private String rolesToString(Set<Role> roles) {
        return roles != null && !roles.isEmpty() ?
                roles.stream()
                        .map(Enum::name)
                        .collect(Collectors.joining(",")) :
                "";
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("X-Auth-Error", message);
        return exchange.getResponse().setComplete();
    }
}

