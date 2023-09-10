package com.api.login.dto;


public record AuthRequest(
        String email,
        String password
) {
}
