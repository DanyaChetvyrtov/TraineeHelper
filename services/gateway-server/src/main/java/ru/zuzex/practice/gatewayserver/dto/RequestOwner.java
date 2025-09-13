package ru.zuzex.practice.gatewayserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zuzex.practice.gatewayserver.security.Role;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestOwner {
    private String userId;
    private String username;
    private Set<Role> roles;
    private Date expiresAt;
}