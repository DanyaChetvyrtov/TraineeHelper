package ru.ci_trainee.authms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ci_trainee.authms.dto.request.JwtRequest;
import ru.ci_trainee.authms.dto.response.JwtResponse;
import ru.ci_trainee.authms.exception.exception.UserNotFoundException;
import ru.ci_trainee.authms.security.jwt.JwtTokenProvider;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public JwtResponse login(JwtRequest loginRequest) {
        var user = userService.getUser(loginRequest.getUsername());
        // user was deactivated
        if(!user.getIsActive()) throw new UserNotFoundException();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        userService.updateLastLogin(user.getId());

        return JwtResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .accessToken(jwtTokenProvider.createAccessToken(user))
                .refreshToken(jwtTokenProvider.createRefreshToken(user.getId(), user.getUsername()))
                .build();
    }

    public JwtResponse refresh(String refreshToken) {
        return jwtTokenProvider.refreshUserTokens(refreshToken);
    }
}
