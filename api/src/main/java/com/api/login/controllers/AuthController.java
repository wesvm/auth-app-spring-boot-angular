package com.api.login.controllers;

import com.api.login.dto.AuthRequest;
import com.api.login.dto.AuthResponse;
import com.api.login.dto.RegisterRequest;
import com.api.login.dto.AuthTfaRequest;
import com.api.login.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody AuthRequest request,
            HttpServletRequest httpRequest){
        var response = authService.login(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, response.accessToken())
                .body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody @Valid RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> confirm(
        @RequestParam("token") String token
    ){
        return ResponseEntity.ok(authService.confirm(token));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authService.refreshToken(request, response);
    }

    @PostMapping("/verify-tfa-code")
    public ResponseEntity<AuthResponse> verifyCode(
            @RequestBody AuthTfaRequest request
    ) {
        var response = authService.verifyTfaCode(request);
        return ResponseEntity.accepted()
                .header(HttpHeaders.AUTHORIZATION, response.accessToken(), response.refreshToken())
                .body(response);
    }

}
