package auth_service.controller;

import auth_service.dto.security.AuthRequest;
import auth_service.dto.security.AuthResponse;
import auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthService authService;

    @Autowired
    public AuthenticationController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/login")
    public AuthResponse loginEndpoint(@RequestBody AuthRequest authRequest) {
        return authService.authenticate(authRequest);
    }

    @PostMapping("/reg")
    public AuthResponse registerEndpoint(@RequestBody AuthRequest authRequest) {
        return authService.reg(authRequest);
    }
}
