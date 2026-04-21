import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { API_BASE_URL } from '../core/api-base';

export interface AdminRegisterUserRequest {
  username: string;
  password: string;
  email: string;
  firstName: string;
  lastName: string;
}

export interface AdminRegisterUserResponse {
  customerId: number;
  username: string;
}

@Injectable({ providedIn: 'root' })
export class AdminService {
  private readonly http = inject(HttpClient);
  private readonly pingUrl = `${API_BASE_URL}/admin/ping`;
  private readonly usersUrl = `${API_BASE_URL}/admin/users`;

  ping(): Observable<string> {
    return this.http.get(this.pingUrl, { responseType: 'text' });
  }

  registerUser(body: AdminRegisterUserRequest): Observable<AdminRegisterUserResponse> {
    return this.http.post<AdminRegisterUserResponse>(this.usersUrl, body);
  }
}
