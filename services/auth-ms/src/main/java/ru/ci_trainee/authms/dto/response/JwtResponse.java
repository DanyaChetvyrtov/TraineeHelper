package ru.ci_trainee.authms.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class JwtResponse {
    private UUID id;
    private String username;
    private String accessToken;
    private String refreshToken;
}
