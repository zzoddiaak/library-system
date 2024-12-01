package auth_service.service;

import auth_service.JWT.JwtService;
import auth_service.config.UserDetailsConfig;
import auth_service.dto.security.AuthRequest;
import auth_service.dto.security.AuthResponse;
import auth_service.entity.User;
import auth_service.exeption.user.UserExistsException;
import auth_service.exeption.user.UserNotFoundLogin;
import auth_service.repository.UserRepository;
import auth_service.service.api.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public AuthResponse authenticate(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword())
        );

        User user = userRepository.findByUsername(authRequest.getLogin())
                .orElseThrow(() -> new UserNotFoundLogin(authRequest.getLogin()));

        String jwtToken = jwtService.generateToken(new UserDetailsConfig(user));

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }


    @Override
    public AuthResponse reg(AuthRequest authRequest) {
        Optional<User> existingUser = userRepository.findByUsername(authRequest.getLogin());
        if (existingUser.isPresent()) {
            throw new UserExistsException();
        }

        User user = User.builder()
                .username(authRequest.getLogin())
                .password(authRequest.getPassword())
                .role("USER")
                .build();

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(new UserDetailsConfig(user));

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

}
