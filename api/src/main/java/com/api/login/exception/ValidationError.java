package com.api.login.exception;

public record ValidationError(
        String field,
        String message
) {
}
