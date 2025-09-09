package ru.ci_trainee.authms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.ci_trainee.authms.validation.OnCreate;
import ru.ci_trainee.authms.validation.OnUpdate;
import ru.ci_trainee.authms.validation.UniqueUsername;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotNull(message = "Id can't be null", groups = OnUpdate.class)
    private UUID id;

    @NotNull(message = "Username must be not null", groups = {OnCreate.class, OnUpdate.class})
    @Length(min = 2, max = 100, message = "Username should be in range between 2 and 255 chars", groups = {OnCreate.class, OnUpdate.class})
    @UniqueUsername(message = "User with such username already exists", groups = OnCreate.class)
    private String username;

    @NotNull(message = "Username must be not null", groups = OnUpdate.class)
    private Boolean isActive;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password must be not null", groups = {OnCreate.class})
    private String password;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password confirmation must be not null", groups = {OnCreate.class})
    private String passwordConfirmation;
}
