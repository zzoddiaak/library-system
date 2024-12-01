package auth_service.controller;

import auth_service.JWT.JwtService;
import auth_service.dto.security.AuthRequest;
import auth_service.dto.security.AuthResponse;
import auth_service.service.api.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthenticationControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    void testValidateTokenValid() throws Exception {
        String validToken = "Bearer valid.jwt.token";
        String username = "user";

        when(jwtService.extractLogin(validToken.substring(7))).thenReturn(username);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(validToken.substring(7), userDetails)).thenReturn(true);

        mockMvc.perform(get("/api/auth/validate")
                        .header("Authorization", validToken))
                .andExpect(status().isOk());
    }

    @Test
    void testValidateTokenInvalid() throws Exception {
        String invalidToken = "Bearer invalid.jwt.token";

        when(jwtService.extractLogin(invalidToken.substring(7))).thenReturn(null);

        mockMvc.perform(get("/api/auth/validate")
                        .header("Authorization", invalidToken))
                .andExpect(status().isUnauthorized());
    }




}
