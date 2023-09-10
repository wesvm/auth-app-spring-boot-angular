import { Injectable } from '@angular/core';

const TOKEN_KEY = 'auth';
const USERNAME_KEY = 'user';
const AUTHORITIES_KEY = 'roles';

@Injectable({
    providedIn: 'root'
})
export class TokenService {

    roles: string = '';

    constructor() { }

    removeToken(): void {
        localStorage.removeItem(TOKEN_KEY);
        localStorage.removeItem(USERNAME_KEY);
    }

    setToken(token: string): void {
        localStorage.removeItem(TOKEN_KEY);
        localStorage.setItem(TOKEN_KEY, token)
    }

    getToken(): string | null {
        return localStorage.getItem(TOKEN_KEY);
    }

    setUserName(username: string): void {
        localStorage.removeItem(USERNAME_KEY);
        localStorage.setItem(USERNAME_KEY, username);
    }

    getUserName(): string | null {
        return localStorage.getItem(USERNAME_KEY);
    }

    getExpiration(token: string): boolean {
        const payload = (JSON.parse(atob(token.split('.')[1]))).exp;
        return (Math.floor((new Date).getTime() / 1000)) >= payload;
    }

    getRoles(): string | null {
        const token = this.getToken();
        if (token) {
            const payload = JSON.parse(atob(token.split('.')[1]));
            if (payload && payload.role) {
                return payload.role;
            }
        }
        return null;
    }

    setAuthorities(authorities: string[]): void {
        localStorage.removeItem(AUTHORITIES_KEY);
        localStorage.setItem(AUTHORITIES_KEY, JSON.stringify(authorities));
    }
}
