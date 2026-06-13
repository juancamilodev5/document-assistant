package com.docassistant.da_backend.auth;

import com.docassistant.da_backend.auth.dto.AuthRequest;
import com.docassistant.da_backend.auth.dto.AuthResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthRequest authRequest) {
        return ResponseEntity.status(201).body(authService.register(authRequest.getEmail(), authRequest.getPassword()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest authRequest) {
        return ResponseEntity.ok(authService.login(authRequest.getEmail(), authRequest.getPassword()));
    }

}
