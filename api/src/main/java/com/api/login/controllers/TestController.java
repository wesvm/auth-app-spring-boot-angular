package com.api.login.controllers;

import com.api.login.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final UserService service;

    @GetMapping
    public ResponseEntity<String> get(){return ResponseEntity.ok("hello from get endpoint");}

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<String> post(Authentication auth){
        UserDetails user = (UserDetails) auth.getPrincipal();
        return ResponseEntity.ok("Hello from post secured endpoint. User: " + user.getUsername());
    }

    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadImage(@RequestParam("file")MultipartFile file) {
        return ResponseEntity.ok(service.uploadProfileImage("blue@mail.com" ,file));
    }

}
