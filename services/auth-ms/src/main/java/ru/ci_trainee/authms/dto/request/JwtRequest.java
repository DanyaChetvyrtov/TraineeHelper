package ru.ci_trainee.authms.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtRequest {
    @NotNull(message = "Username must be not null")
    private String username;
    @NotNull(message = "Password must be not null")
    private String password;
}
