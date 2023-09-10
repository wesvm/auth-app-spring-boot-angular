package com.api.login.dto;

public record UserInfoResponse(
        String email,
        String name,
        String profileImage,
        boolean mfaEnabled
) {
}
