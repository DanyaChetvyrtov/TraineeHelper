package ru.ci_trainee.authms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import ru.ci_trainee.authms.dto.request.JwtRequest;
import ru.ci_trainee.authms.dto.response.JwtResponse;
import ru.ci_trainee.authms.exception.exception.UserNotFoundException;
import ru.ci_trainee.authms.model.User;
import ru.ci_trainee.authms.security.jwt.JwtTokenProvider;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    @Test
    @Transactional
    void login_ShouldReturnJwtResponse_WhenCredentialsValid() {
        // Arrange
        var userId = UUID.randomUUID();
        var user = User.builder()
                .id(userId)
                .username("testuser")
                .password("encodedPass")
                .isActive(true)
                .build();

        var request = new JwtRequest("testuser", "password");
        var expectedResponse = JwtResponse.builder()
                .id(userId)
                .username("testuser")
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();

        when(userService.getUser("testuser")).thenReturn(user);
        when(jwtTokenProvider.createAccessToken(user)).thenReturn("accessToken");
        when(jwtTokenProvider.createRefreshToken(userId, "testuser")).thenReturn("refreshToken");

        JwtResponse actualResponse = authService.login(request);

        // Assert
        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("testuser", "password")
        );
        verify(userService).updateLastLogin(userId);
    }

    @Test
    void login_ShouldThrowUserNotFoundException_WhenUserInactive() {
        var inactiveUser = User.builder()
                .username("inactive")
                .isActive(false)
                .build();

        when(userService.getUser("inactive")).thenReturn(inactiveUser);

        assertThatThrownBy(() -> authService.login(new JwtRequest("inactive", "pass")))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void login_ShouldThrowBadCredentials_WhenAuthenticationFails() {
        // Arrange
        User user = User.builder()
                .username("testuser")
                .isActive(true)
                .build();

        when(userService.getUser("testuser")).thenReturn(user);
        doThrow(new BadCredentialsException("Invalid credentials"))
                .when(authenticationManager).authenticate(any());

        // Act & Assert
        assertThatThrownBy(() -> authService.login(new JwtRequest("testuser", "wrongpass")))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Invalid credentials");
    }


    @Test
    void refresh_ShouldReturnNewTokens_WhenRefreshTokenValid() {
        // Arrange
        String refreshToken = "valid.refresh.token";
        JwtResponse expectedResponse = JwtResponse.builder()
                .accessToken("newAccess")
                .refreshToken("newRefresh")
                .build();

        when(jwtTokenProvider.refreshUserTokens(refreshToken)).thenReturn(expectedResponse);

        // Act
        JwtResponse actualResponse = authService.refresh(refreshToken);

        // Assert
        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(jwtTokenProvider).refreshUserTokens(refreshToken);
    }
}
