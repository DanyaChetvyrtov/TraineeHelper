package ru.ci_trainee.authms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ci_trainee.authms.dto.UserDto;
import ru.ci_trainee.authms.dto.request.JwtRequest;
import ru.ci_trainee.authms.dto.response.JwtResponse;
import ru.ci_trainee.authms.mapper.UserMapper;
import ru.ci_trainee.authms.service.AuthService;
import ru.ci_trainee.authms.service.UserService;
import ru.ci_trainee.authms.validation.OnCreate;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    @Operation(
            summary = "Login user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad credentials"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    public ResponseEntity<JwtResponse> login(@RequestBody @Validated JwtRequest loginRequest) {
        return ResponseEntity.ok().body(authService.login(loginRequest));
    }


    @PostMapping("/register")
    @Operation(
            summary = "Register user",
            responses = {
                    @ApiResponse(responseCode = "201", description = "New user created"),
                    @ApiResponse(responseCode = "400", description = "Validation failed"),
                    @ApiResponse(responseCode = "400", description = "Passwords mismatch"),
                    @ApiResponse(responseCode = "400", description = "Id should not be specified")
            }
    )
    public ResponseEntity<UserDto> register(@RequestBody @Validated(OnCreate.class) UserDto userDto) {
        var user = userMapper.toEntity(userDto);
        userDto = userMapper.toDto(userService.create(user, userDto.getPasswordConfirmation()));
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }


    @Operation(
            summary = "Refresh tokens",
            responses = {
                    @ApiResponse(responseCode = "200", description = "New tokens")
            }
    )
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@RequestBody String refreshToken) {
        return ResponseEntity.ok().body(authService.refresh(refreshToken));
    }
}
