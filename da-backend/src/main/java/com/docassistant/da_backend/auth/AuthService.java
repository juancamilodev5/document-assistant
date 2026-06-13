package com.docassistant.da_backend.auth;

import com.docassistant.da_backend.auth.dto.AuthResponse;
import com.docassistant.da_backend.config.JwtService;
import com.docassistant.da_backend.user.Role;
import com.docassistant.da_backend.user.User;
import com.docassistant.da_backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthResponse register(String email, String password) {

        // verify if email exists
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent())
            throw new RuntimeException("user already exists");

        User userCreated = new User();
        userCreated.setEmail(email);
        userCreated.setRole(Role.USER);
        userCreated.setPassword(passwordEncoder.encode(password));
        userRepository.save(userCreated);

        // user authenticated.
        return AuthResponse
                .builder()
                .token(jwtService.generateToken(userCreated))
                .build();
    }

    public AuthResponse login(String email, String password) {

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty())
            throw new RuntimeException("user not found");

        User user = optionalUser.get();

        if (!passwordEncoder.matches(password, user.getPassword()))
            // intentional ambiguous answer
            throw new RuntimeException("user not found");

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .build();
    }

}
