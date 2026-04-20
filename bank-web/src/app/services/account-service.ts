import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';

import { API_BASE_URL } from '../core/api-base';
import { Account } from '../models/account';

@Injectable({
  providedIn: 'root',
})
export class AccountService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${API_BASE_URL}/accounts`;

  getBalance(accountNumber: string): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/${accountNumber}/balance`);
  }

  getAccount(accountNumber: string): Observable<Account> {
    return this.http.get<Account>(`${this.apiUrl}/${accountNumber}`);
  }

  getAllAccounts(): Observable<Account[]> {
    return this.http.get<Account[]>(this.apiUrl).pipe(
      map((accounts) =>
        [...accounts].sort((a, b) => a.accountNumber.localeCompare(b.accountNumber))
      )
    );
  }
}
