import { HttpInterceptorFn } from '@angular/common/http';

import { readStoredAccessToken } from './auth-token.storage';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  if (req.url.includes('/api/auth/login')) {
    return next(req);
  }
  const token = readStoredAccessToken();
  if (token) {
    req = req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
  }
  return next(req);
};
