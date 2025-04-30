import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { UserContextService } from '../services/user-context.service';
import { Role } from '../models/Role';

export const adminAuthGuard: CanActivateFn = (route, state) => {
  const userContextService = inject(UserContextService);
  if (userContextService.getUserRoles().includes(Role.ADMIN)) {
    return true;
  }
  inject(Router).navigate(["/bookshelf"]);
  return false;
};
