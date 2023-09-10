package com.api.login.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleException(
            ResourceNotFoundException e,
            HttpServletRequest request) {
        return buildErrorResponse(request, HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleUnauthorizedException(
            BadCredentialsException e, HttpServletRequest request) {
        return buildErrorResponse(request, HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<ApiError> handleException(
            InsufficientAuthenticationException e,
            HttpServletRequest request) {
        return buildErrorResponse(request, HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiError> handleInvalidDataException(
            Exception e, HttpServletRequest request) {
        return buildErrorResponse(request, HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(UploadProfileImageException.class)
    public ResponseEntity<ApiError> handleUploadProfileImageException(
            InsufficientAuthenticationException e, HttpServletRequest request) {
        return buildErrorResponse(request, HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleUnauthorizedException(
            MethodArgumentNotValidException e, HttpServletRequest request) {

        List<ValidationError> errors = e.getFieldErrors().stream()
                .map(fieldError -> new ValidationError(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        return buildErrorResponse(request, HttpStatus.BAD_REQUEST,
                "Validation failed for one or more fields", errors);
    }

    private ResponseEntity<ApiError> buildErrorResponse(
            HttpServletRequest request, HttpStatus status, String message) {
        return buildErrorResponse(request, status, message, null);
    }

    private ResponseEntity<ApiError> buildErrorResponse(
            HttpServletRequest request, HttpStatus status, String message, List<ValidationError> errors) {
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                message,
                status.value(),
                LocalDateTime.now(),
                errors
        );
        return new ResponseEntity<>(apiError, status);
    }
}
