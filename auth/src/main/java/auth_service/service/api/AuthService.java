package auth_service.service.api;

import auth_service.dto.security.AuthRequest;
import auth_service.dto.security.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    AuthResponse authenticate(AuthRequest authRequest);
    AuthResponse reg(AuthRequest authRequest);
}
