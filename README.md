# Auth App - Spring Boot & Angular

This application is an authentication system built using Angular for the client-side and Spring Boot for the API.

## Features

- **User Registration**: Users can create new accounts by providing their details. Upon registration, a verification link is sent to their email for account validation.
- **Account Verification**: Users must verify their email addresses before they can log in.
- **User Login**: Registered users can log in to their accounts using their validated email addresses and passwords.
- **Two-Factor Authentication (2FA)**: For enhanced security, users have the option to enable Two-Factor Authentication.

## Technologies

- **JWT (JSON Web Tokens)**: Used for secure authentication and authorization.
- **Two-Factor Authentication (2FA)**: Provides an additional layer of security for user accounts.
- **SMTP**: Enables the sending of verification emails during the registration process.
- **Flyway**: Handles database migrations to ensure consistent data structure across environments.
- **MySQL**: The chosen database for persistent storage.
