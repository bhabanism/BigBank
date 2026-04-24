import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';

import { API_BASE_URL } from './api-base';
import { clearStoredAccessToken, readStoredAccessToken, writeStoredAccessToken } from './auth-token.storage';

/** Bank shell (host) public URL; this MFE has no /login route, so sign-out always goes to the shell. */
const SHELL_LOGIN = 'http://localhost:4200/login';

export interface LoginResponse {
  accessToken: string;
  tokenType: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);

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
    window.location.assign(SHELL_LOGIN);
  }

  /** Call when the API returns 401 (e.g. expired JWT). */
  onUnauthorized(): void {
    this.logout();
  }
}
