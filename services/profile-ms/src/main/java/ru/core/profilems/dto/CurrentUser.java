package ru.core.profilems.dto;

import java.util.Arrays;
import java.util.Set;

public class CurrentUser {
    private static final ThreadLocal<String> userId = new ThreadLocal<>();
    private static final ThreadLocal<String> username = new ThreadLocal<>();
    private static final ThreadLocal<Set<Role>> roles = new ThreadLocal<>();

    public void setUserId(String id) {
        userId.set(id);
    }

    public String getUserId() {
        return userId.get();
    }

    public void setUsername(String name) {
        username.set(name);
    }

    public String getUsername() {
        return username.get();
    }

    public void setRoles(Set<Role> userRoles) {
        roles.set(userRoles);
    }

    public Set<Role> getRoles() {
        return roles.get();
    }

    public void clear() {
        userId.remove();
        username.remove();
        roles.remove();
    }

    public boolean hasRole(Role role) {
        Set<Role> userRoles = roles.get();
        return userRoles != null && userRoles.contains(role);
    }

    public boolean hasAnyRole(Role... requiredRoles) {
        Set<Role> userRoles = roles.get();
        if(userRoles == null) return false;

        return Arrays.stream(requiredRoles).anyMatch(userRoles::contains);
    }
}
