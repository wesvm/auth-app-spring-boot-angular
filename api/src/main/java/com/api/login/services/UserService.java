package com.api.login.services;

import com.api.login.dto.AuthTfaRequest;
import com.api.login.dto.TfaResponse;
import com.api.login.dto.UserInfoResponse;
import com.api.login.dto.UserListResponse;
import com.api.login.exception.DuplicateResourceException;
import com.api.login.exception.ResourceNotFoundException;
import com.api.login.exception.UploadProfileImageException;
import com.api.login.models.Token;
import com.api.login.models.TokenType;
import com.api.login.models.User;
import com.api.login.repositories.TokenRepository;
import com.api.login.repositories.UserRepository;
import com.api.login.utils.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final TfaService tfaService;
    private final TokenRepository tokenRepository;

    public Page<UserListResponse> getAll(Pageable pageable){
        Page<User> users = repository.findAllByEnabledTrue(pageable);
        return users.map(UserListResponse::new);
    }

    public UserInfoResponse getInfo(String email) {
        User user = repository.findByEmailAndEnabledTrue(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new UserInfoResponse(
            user.getEmail(), 
            user.getName(), 
            user.getProfileImage(), 
            user.isMfaEnabled()
        );
    }

    public TfaResponse enableTfa(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setMfaEnabled(false);
        user.setSecret(tfaService.generateNewSecret());
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

        repository.save(user);
        tokenRepository.save(token);
        var qrCode = tfaService.generateQrCodeImageUri(user.getSecret());

        return new TfaResponse(qrCode, generatedToken);
    }

    public String confirmTfa(String email, AuthTfaRequest request){
        if(!email.equals(request.email())){
            throw new RuntimeException("user? " + email + " - " +request.email());
        }

        User user = repository
                .findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Token savedTicket = tokenRepository.findByToken(request.ticket())
                .orElseThrow(()->new ResourceNotFoundException("Ticket not found"));

        if (!savedTicket.getUser().equals(user)) {
            throw new BadCredentialsException("Invalid ticket for the user.");
        }

        if (savedTicket.isRevoked() && savedTicket.isExpired()) {
            throw new BadCredentialsException("Ticket revoked or expired.");
        }

        if(LocalDateTime.now().isAfter(savedTicket.getExpiresAt())){
            user.setSecret(null);
            repository.save(user);
            throw new BadCredentialsException("Ticket has expired, create a new ticket.");
        }

        if (tfaService.isOtpNotValid(user.getSecret(), request.code())) {
            throw new BadCredentialsException("Invalid TFA code.");
        }

        user.setMfaEnabled(true);
        tokenRepository.delete(savedTicket);
        repository.save(user);

        return "great";
    }

    public String uploadProfileImage(String email, MultipartFile file) {
        if (!ImageUtils.isImage(file)) {
            throw new UploadProfileImageException("Invalid image file.");
        }

        User user = repository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        String imageUrl = ImageUtils.uploadImageToAPI(file);
        if(imageUrl == null){
            throw new UploadProfileImageException("Error uploading image.");
        }

        user.setProfileImage(imageUrl);
        repository.save(user);

        return imageUrl;
    }
}
