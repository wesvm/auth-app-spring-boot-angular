package com.api.login.dto;

public record TfaResponse(
        String qrcode,
        String ticket
) {
}
