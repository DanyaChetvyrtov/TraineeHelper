package ru.core.profilems.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.core.profilems.constants.CustomHeaders;
import ru.core.profilems.dto.CurrentUser;
import ru.core.profilems.dto.Role;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserInfoInterceptor implements HandlerInterceptor {
    private final CurrentUser currentUser;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        currentUser.setUserId(request.getHeader(CustomHeaders.USER_ID));
        currentUser.setUsername(request.getHeader(CustomHeaders.USERNAME));
        currentUser.setRoles(extractRoles(request.getHeader(CustomHeaders.USER_ROLES)));

        log.debug("User context set: userId={}, username={}, roles={}",
                currentUser.getUserId(), currentUser.getUsername(), currentUser.getRoles());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        currentUser.clear();
    }

    private Set<Role> extractRoles(String rolesHeader) {
        if (rolesHeader != null && !rolesHeader.isBlank()) {
            return Arrays.stream(rolesHeader.split(","))
                    .map(String::trim)
                    .filter(role -> !role.isEmpty())
                    .map(role -> {
                        try {
                            return Role.valueOf(role);
                        } catch (IllegalArgumentException iae) {
                            log.warn("Unknown role in header: {}", role);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
        } else return Collections.emptySet();
    }
}
