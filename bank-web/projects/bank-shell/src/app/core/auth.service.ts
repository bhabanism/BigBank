import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';

import { API_BASE_URL } from './api-base';
import { clearStoredAccessToken, readStoredAccessToken, writeStoredAccessToken } from './auth-token.storage';

export interface LoginResponse {
  accessToken: string;
  tokenType: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly loginUrl = `${API_BASE_URL}/auth/login`;

  isLoggedIn(): boolean {
    return !!readStoredAccessToken();
  }

  login(username: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(this.loginUrl, { username, password }).pipe(
      tap((res) => writeStoredAccessToken(res.accessToken)),
    );
  }

  logout(): void {
    clearStoredAccessToken();
    void this.router.navigateByUrl('/login');
  }
}
