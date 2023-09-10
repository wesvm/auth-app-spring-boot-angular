package com.api.login.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UploadProfileImageException extends RuntimeException {
    public UploadProfileImageException(String message) {
        super(message);
    }
}
