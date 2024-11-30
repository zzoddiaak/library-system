package auth_service.controller;

import auth_service.JWT.JwtService;
import auth_service.dto.security.AuthRequest;
import auth_service.dto.security.AuthResponse;
import auth_service.dto.security.UserDetailsResponse;
import auth_service.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthenticationController {
    private final AuthService authService;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken(@RequestHeader("Authorization") String tokenHeader) {
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = tokenHeader.substring(7);

        String username = jwtService.extractLogin(token);

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (jwtService.isTokenValid(token, userDetails)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


    @PostMapping("/login")
    public AuthResponse loginEndpoint(@RequestBody AuthRequest authRequest) {
        return authService.authenticate(authRequest);
    }

    @PostMapping("/reg")
    public AuthResponse registerEndpoint(@RequestBody AuthRequest authRequest) {
        return authService.reg(authRequest);
    }
    @GetMapping("/user/{username}")
    public ResponseEntity<UserDetailsResponse> getUserDetails(@PathVariable String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UserDetailsResponse response = new UserDetailsResponse(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        return ResponseEntity.ok(response);
    }
}
