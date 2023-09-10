package com.api.login.services;

import com.api.login.config.JwtService;
import com.api.login.dto.AuthRequest;
import com.api.login.dto.AuthResponse;
import com.api.login.dto.RegisterRequest;
import com.api.login.dto.AuthTfaRequest;
import com.api.login.exception.DuplicateResourceException;
import com.api.login.exception.ResourceNotFoundException;
import com.api.login.models.Token;
import com.api.login.models.TokenType;
import com.api.login.models.User;
import com.api.login.models.UserRole;
import com.api.login.repositories.TokenRepository;
import com.api.login.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String CONFIRMATION_URL = "http://localhost:8080/api/auth/confirm?token=%s";

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TfaService tfaService;

    public AuthResponse login(AuthRequest request){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(), request.password())
        );
        User user = (User) auth.getPrincipal();

        if(user.isMfaEnabled()){
            var generatedToken  = UUID.randomUUID().toString();
            var createdAt = LocalDateTime.now();
            var expiresAt = createdAt.plusMinutes(10);

            Token token = Token.builder()
                    .user(user)
                    .token(generatedToken)
                    .tokenType(TokenType.MFA)
                    .createdAt(createdAt)
                    .expiresAt(expiresAt)
                    .validatedAt(null)
                    .build();
            tokenRepository.save(token);

            return new AuthResponse(true, generatedToken);
        }

        var userRole = user.getRole().name().toLowerCase();
        var accessToken  = jwtService.generateToken(user, userRole);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);

        return new AuthResponse(accessToken, refreshToken);
    }

    public String register(RegisterRequest request){
        if (userRepository.existsUserByEmail(request.email())){
            throw new DuplicateResourceException("A user already exist with the same email");
        }

        User user = User.builder()
                .name(request.name().trim())
                .email(request.email().trim())
                .password(passwordEncoder.encode(request.password().trim()))
                .role(UserRole.USER)
                .mfaEnabled(false)
                .profileImage("https://i.postimg.cc/nct46HnL/avatar.png")
                .enabled(false)
                .locked(false)
                .build();

        var savedUser = userRepository.save(user);
        var token = saveValidationEmailToken(savedUser);

        try {
            emailService.send(user, token);
        }catch (MessagingException e){
            e.printStackTrace();
        }

        return "Email verification has send, check your email!";
    }

    public String confirm(String token){
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(()->new ResourceNotFoundException("Token not found"));
        if (savedToken.getValidatedAt() != null) {
            return "Email already confirmed";
        }
        if(LocalDateTime.now().isAfter(savedToken.getExpiresAt())){
            return "Token expired";
        }

        User user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        savedToken.setRevoked(true);
        savedToken.setExpired(true);
        tokenRepository.save(savedToken);

        return "Your account has been successfully activated";
    }

    public AuthResponse verifyTfaCode(
            AuthTfaRequest request
    ) {
        User user = userRepository
                .findByEmailAndEnabledTrue(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Token savedTicket = tokenRepository.findByToken(request.ticket())
                .orElseThrow(()->new ResourceNotFoundException("Ticket not found"));

        if(!user.isMfaEnabled()){
            throw new RuntimeException("user has not mfa enabled");
        }

        if (!savedTicket.getUser().equals(user)) {
            throw new BadCredentialsException("Invalid ticket for the user");
        }

        if (savedTicket.isRevoked() && savedTicket.isExpired()) {
            throw new BadCredentialsException("Ticket revoked or expired");
        }

        if(LocalDateTime.now().isAfter(savedTicket.getExpiresAt())){
            throw new BadCredentialsException("Ticket has expired");
        }

        if (tfaService.isOtpNotValid(user.getSecret(), request.code())) {
            throw new BadCredentialsException("Invalid TFA code");
        }

        savedTicket.setExpired(true);
        savedTicket.setRevoked(true);
        tokenRepository.save(savedTicket);

        var userRole = user.getRole().name().toLowerCase();
        var accessToken  = jwtService.generateToken(user, userRole);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);

        return new AuthResponse(accessToken, refreshToken);
    }

    private Token saveValidationEmailToken(User user) {
        var generatedToken  = UUID.randomUUID().toString();
        var createdAt = LocalDateTime.now();
        var expiresAt = createdAt.plusMinutes(30);

        Token token = Token.builder()
                .user(user)
                .token(generatedToken)
                .tokenType(TokenType.EMAIL_VERIFICATION)
                .createdAt(createdAt)
                .expiresAt(expiresAt)
                .validatedAt(null)
                .build();
        tokenRepository.save(token);

        return token;
    }

    private void saveUserToken(User user, String accessToken) {
        Token token = Token.builder()
                .user(user)
                .token(accessToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User users) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(users.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmailAndEnabledTrue(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse =  new AuthResponse(accessToken, refreshToken);

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}

