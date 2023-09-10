import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { TokenService } from '../token/token.service';

export const roleGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const tokenService = inject(TokenService);

  const roles = tokenService.getRoles();

  if (roles) {
    const isAuthorized = roles.includes(route.data['role']);
    if (isAuthorized) {
      return true;
    }
  }

  router.navigate(['']);
  return false;
};
