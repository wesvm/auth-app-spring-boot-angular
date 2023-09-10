import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegisterRequest } from '../models/register-request';
import { AuthRequest } from '../models/auth-request';
import { AuthResponse } from '../models/auth-response';
import { Observable } from 'rxjs';
import { TfaRequest } from '../models/tfa-request';

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    //private baseUrl = "http://localhost:8080/api/auth"
    private baseUrl = "http://192.168.1.84:8080/api/auth";

    constructor(
        private http: HttpClient
    ) { }

    register(
        registerRequest: RegisterRequest
    ) {
        return this.http.post<any>(`${this.baseUrl}/register`, registerRequest);
    }

    login(
        authRequest: AuthRequest
    ) {
        return this.http.post<AuthResponse>
            (`${this.baseUrl}/login`, authRequest);
    }

    verifyTfaCode(
        tfaRequest: TfaRequest
    ) {
        return this.http.post<AuthResponse>
            (`${this.baseUrl}/verify-tfa-code`, tfaRequest);
    }

    logout(): Observable<void> {
        return this.http.get<any>(`${this.baseUrl}/logout`);
    }

}
