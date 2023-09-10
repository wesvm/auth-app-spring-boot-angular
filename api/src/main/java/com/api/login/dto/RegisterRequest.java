package com.api.login.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank
        @Size(max = 50)
        String name,
        @NotBlank
        @Email
        @Size(max = 100)
        String email,
        @NotBlank
        @Size(max = 255, min = 5)
        String password,
        @NotBlank
        String confirmPassword
) {
        @AssertTrue(message = "passwords do not match")
        private boolean isPasswordsMatch(){
                return password.equals(confirmPassword);
        }
}
