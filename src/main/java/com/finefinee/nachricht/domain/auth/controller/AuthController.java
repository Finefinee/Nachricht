package com.finefinee.nachricht.domain.auth.controller;


import com.finefinee.nachricht.domain.auth.dto.LoginRequest;
import com.finefinee.nachricht.domain.auth.dto.RefreshRequest;
import com.finefinee.nachricht.domain.auth.dto.RegisterRequest;
import com.finefinee.nachricht.domain.auth.service.AuthService;
import com.finefinee.nachricht.global.security.jwt.dto.JwtResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok().body(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@RequestBody RefreshRequest request) {
        return ResponseEntity.ok().body(authService.refresh(request));
    }

}