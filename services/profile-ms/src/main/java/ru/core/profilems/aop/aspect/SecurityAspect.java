package ru.core.profilems.aop.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import ru.core.profilems.aop.annotation.CheckProfileOwnership;
import ru.core.profilems.aop.annotation.HasPermission;
import ru.core.profilems.aop.annotation.HasRole;
import ru.core.profilems.dto.CurrentUser;
import ru.core.profilems.dto.Role;
import ru.core.profilems.exception.exception.AccessDeniedException;

import java.util.UUID;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class SecurityAspect {
    private final CurrentUser currentUser;

    @Around("@annotation(hasRole)")
    public Object checkRole(ProceedingJoinPoint joinPoint, HasRole hasRole) throws Throwable {
        Role requiredRole = hasRole.value();

        if (!currentUser.hasRole(requiredRole)) {
            log.warn("Access denied for user {}. Required role: {}",
                    currentUser.getUsername(), requiredRole);
            throw new AccessDeniedException("Access denied");
        }

        return joinPoint.proceed();
    }

    @Around("@annotation(checkOwnership)")
    public Object checkOwnership(ProceedingJoinPoint joinPoint, CheckProfileOwnership checkOwnership) throws Throwable {
        UUID profileId = extractProfileIdFromArgs(joinPoint, checkOwnership.profileIdParam());

        if (!isProfileOwner(profileId)) {
            log.warn("Access denied for user {}. Trying to access profile {}",
                    currentUser.getUsername(), profileId);
            throw new AccessDeniedException("Access denied. You can only access your own profile");
        }

        return joinPoint.proceed();
    }

    @Around("@annotation(hasPermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, HasPermission hasPermission) throws Throwable {
        if (currentUser.hasRole(Role.ROLE_ADMIN)) {
            log.debug("Admin access granted for user: {}", currentUser.getUsername());
            return joinPoint.proceed();
        }

        UUID profileId = extractProfileIdFromArgs(joinPoint, hasPermission.profileIdParam());

        if (isProfileOwner(profileId)) {
            log.debug("Owner access granted for user: {} to profile: {}", currentUser.getUsername(), profileId);
            return joinPoint.proceed();
        }

        log.warn("Access denied for user: {}. Neither profile owner nor ADMIN", currentUser.getUsername());
        throw new AccessDeniedException("Access denied. You need to be profile owner or ADMIN");
    }


    private UUID extractProfileIdFromArgs(ProceedingJoinPoint joinPoint, String paramName) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < parameterNames.length; i++)
            if (parameterNames[i].equals(paramName) && args[i] instanceof UUID validId) return validId;

        throw new IllegalArgumentException("Profile ID parameter '" + paramName + "' not found or not UUID");
    }

    private boolean isProfileOwner(UUID profileId) {
        String currentUserId = currentUser.getUserId();

        if (currentUserId == null)
            return false;

        try {
            UUID currentUserUUID = UUID.fromString(currentUserId);
            return currentUserUUID.equals(profileId);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid user ID format: {}", currentUserId);
            return false;
        }
    }
}
