import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ApiErrorResponse } from 'src/app/models/api-response';
import { RegisterRequest } from 'src/app/models/register-request';
import { AuthService } from 'src/app/services/auth.service';

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.css']
})
export class RegisterComponent {

    registerRequest: RegisterRequest = {
        name: '',
        email: '',
        password: '',
        confirmPassword: ''
    };

    responseErrors: ApiErrorResponse = { message: '', errors: [] };

    constructor(private router: Router, private authService: AuthService) { }

    register() {
        this.authService.register(this.registerRequest).subscribe({
            next: (response) => {
                alert('great! check your email');
                console.log('great! check your email');
                this.router.navigate(['/login'])
            },
            error: (error) => {
                this.responseErrors = error.error;
            }
        });
    }

    closeErrorMessage() {
        this.responseErrors.message = '';
        this.responseErrors.errors = [];
    }

}
