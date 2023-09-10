package com.api.login.controllers;

import com.api.login.dto.AuthTfaRequest;
import com.api.login.dto.TfaResponse;
import com.api.login.dto.UserListResponse;
import com.api.login.dto.UserInfoResponse;
import com.api.login.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class UserController {

    private final UserService service;

    @GetMapping
    public Page<UserListResponse> getAllUsers(@PageableDefault(size = 20)Pageable pageable){
        return service.getAll(pageable);
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfoResponse> info(Authentication auth){
        UserDetails user = (UserDetails) auth.getPrincipal();
        var info = service.getInfo(user.getUsername());
        return ResponseEntity.ok().body(info);
    }

    @GetMapping("/activate-tfa")
    public ResponseEntity<TfaResponse> activateTfa(Authentication auth){
        UserDetails user = (UserDetails) auth.getPrincipal();
        return ResponseEntity.ok(service.enableTfa(user.getUsername()));
    }

    @PostMapping("/validate-tfa")
    public ResponseEntity<String> activateTfa(Authentication auth, @RequestBody AuthTfaRequest request){
        UserDetails user = (UserDetails) auth.getPrincipal();
        return ResponseEntity.ok(service.confirmTfa(user.getUsername(), request));
    }

    @PostMapping("/info/upload-image")
    public ResponseEntity<String> uploadProfileImage(
            Authentication auth,
            @RequestParam("file") MultipartFile file){
        UserDetails user = (UserDetails) auth.getPrincipal();
        return ResponseEntity.ok(service.uploadProfileImage(user.getUsername(), file));
    }

}
