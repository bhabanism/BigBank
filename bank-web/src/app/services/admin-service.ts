import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { API_BASE_URL } from '../core/api-base';

@Injectable({ providedIn: 'root' })
export class AdminService {
  private readonly http = inject(HttpClient);
  private readonly url = `${API_BASE_URL}/admin/ping`;

  ping(): Observable<string> {
    return this.http.get(this.url, { responseType: 'text' });
  }
}
