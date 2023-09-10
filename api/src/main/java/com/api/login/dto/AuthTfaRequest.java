package com.api.login.dto;

public record AuthTfaRequest(
        String email,
        String code,
        String ticket
) {
}
