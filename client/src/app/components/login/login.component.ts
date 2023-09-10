import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ApiErrorResponse } from 'src/app/models/api-response';
import { AuthRequest } from 'src/app/models/auth-request';
import { AuthResponse } from 'src/app/models/auth-response';
import { TfaRequest } from 'src/app/models/tfa-request';
import { AuthService } from 'src/app/services/auth.service';
import { TokenService } from 'src/app/services/token/token.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent {
    errorMessage = ''

    authRequest: AuthRequest = {
        email: '',
        password: '',
    };

    apiErrorResponse: ApiErrorResponse = { message: '' };
    otpCode = '';
    ticket = '';
    authResponse: AuthResponse = { access_token: '', refresh_token: '', mfa_enabled: false };
    mfaEnabled = false;

    constructor(
        private authService: AuthService,
        private router: Router,
        private tokenService: TokenService
    ) {
    }

    ngOnInit(): void {
        this.isLogged();
    }

    private isLogged() {
        const token = this.tokenService.getToken();
        if (token) this.router.navigate(['']);
    }

    auth() {
        this.authService.login(this.authRequest).subscribe({
            next: (response: AuthResponse) => {
                this.authResponse = response;
                if (this.authResponse.mfa_enabled) {
                    this.ticket = this.authResponse.access_token;
                    this.mfaEnabled = true;
                    this.errorMessage = '';
                } else {
                    this.tokenService.setToken(this.authResponse.access_token);
                    this.tokenService.setUserName(this.authRequest.email as string);
                    this.router.navigate(['']);
                }
            },
            error: (error) => {
                console.error('Login error:', error);
                this.apiErrorResponse = error.error;
                this.errorMessage = this.apiErrorResponse.message;
            }
        });
    }

    verifyCode() {
        const tfaRequest: TfaRequest = {
            email: this.authRequest.email,
            code: this.otpCode,
            ticket: this.ticket
        };
        this.authService.verifyTfaCode(tfaRequest).subscribe({
            next: (response: AuthResponse) => {
                this.tokenService.setToken(response.access_token as string);
                this.tokenService.setUserName(this.authRequest.email as string);
                this.router.navigate(['']);
            },
            error: (error) => {
                console.error('tfa validate error:', error);
                this.apiErrorResponse = error.error;
                this.errorMessage = this.apiErrorResponse.message;
            }
        });
    }

    closeErrorMessage() {
        this.errorMessage = '';
    }
}
