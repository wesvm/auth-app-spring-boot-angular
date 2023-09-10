import { CanActivateFn, Router } from '@angular/router';
import { TokenService } from '../token/token.service';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = (route, state) => {
    const router = inject(Router);
    const tokenService = inject(TokenService);

    const storeUser = tokenService.getUserName();

    if (storeUser) {
        const token = tokenService.getToken();
        if (token) {
            const isTokenNonExpired = tokenService.getExpiration(token);
            if (!isTokenNonExpired) return true;
        }
    }

    router.navigate(['login']);
    return false;
}