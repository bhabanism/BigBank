import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { Account } from '../models/account';

@Injectable({
  providedIn: 'root',
})
export class AccountService {
  private apiUrl = 'http://localhost:8081/api/accounts';

  constructor(private http: HttpClient) { }

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
