package com.api.login.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record AuthResponse(
        @JsonProperty("mfa_enabled") Boolean mfa,
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("refresh_token") String refreshToken
) {
    public AuthResponse(boolean mfa, String accessToken) {
        this(mfa, accessToken, null);
    }
    public AuthResponse(String accessToken, String refreshToken) {
        this(null, accessToken, refreshToken);
    }

}
