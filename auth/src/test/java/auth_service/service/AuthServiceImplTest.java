package auth_service.service;

import auth_service.JWT.JwtService;
import auth_service.dto.security.AuthRequest;
import auth_service.dto.security.AuthResponse;
import auth_service.entity.User;
import auth_service.exeption.user.UserExistsException;
import auth_service.exeption.user.UserNotFoundLogin;
import auth_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    private AuthRequest authRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authRequest = new AuthRequest("user", "password");
    }

    @Test
    void testAuthenticateSuccess() {
        // Create user using the builder pattern
        User user = User.builder()
                .username("user")
                .password("password")
                .role("USER")
                .build();
        when(userRepository.findByUsername("user")).thenReturn(java.util.Optional.of(user));
        when(jwtService.generateToken(any())).thenReturn("valid.jwt.token");

        AuthResponse authResponse = authService.authenticate(authRequest);

        assertNotNull(authResponse);
        assertEquals("valid.jwt.token", authResponse.getToken());
    }

    @Test
    void testAuthenticateUserNotFound() {
        when(userRepository.findByUsername("user")).thenReturn(java.util.Optional.empty());

        assertThrows(UserNotFoundLogin.class, () -> authService.authenticate(authRequest));
    }

    @Test
    void testRegisterSuccess() {
        User user = User.builder()
                .username("newUser")
                .password("password")
                .role("USER")
                .build();
        when(userRepository.findByUsername("newUser")).thenReturn(java.util.Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any())).thenReturn("valid.jwt.token");

        AuthResponse authResponse = authService.reg(authRequest);

        assertNotNull(authResponse);
        assertEquals("valid.jwt.token", authResponse.getToken());
    }

    @Test
    void testRegisterUserExists() {
        User existingUser = User.builder()
                .username("existingUser")
                .password("password")
                .role("USER")
                .build();

        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(existingUser));

        assertThrows(UserExistsException.class, () -> authService.reg(new AuthRequest("existingUser", "password")));
    }

}
