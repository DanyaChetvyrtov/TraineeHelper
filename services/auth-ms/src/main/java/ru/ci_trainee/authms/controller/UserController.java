package ru.ci_trainee.authms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ci_trainee.authms.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/{userId}")
    public ResponseEntity<HttpStatus> makeAdmin(@PathVariable UUID userId) {
        userService.makeAdmin(userId);
        return ResponseEntity.noContent().build();
    }
}
