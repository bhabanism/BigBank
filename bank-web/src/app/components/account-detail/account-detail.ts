import { AsyncPipe, DecimalPipe } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { catchError, distinctUntilChanged, map, of, switchMap } from 'rxjs';

import { AccountService } from '../../services/account-service';

@Component({
  selector: 'app-account-detail',
  imports: [AsyncPipe, DecimalPipe, RouterLink],
  templateUrl: './account-detail.html',
  styleUrl: './account-detail.css',
})
export class AccountDetail {
  private readonly route = inject(ActivatedRoute);
  private readonly accountService = inject(AccountService);

  protected readonly balanceFromEndpoint = signal<number | null>(null);
  protected readonly balanceLoading = signal(false);
  protected readonly balanceError = signal<string | null>(null);

  protected readonly account$ = this.route.paramMap.pipe(
    map((p) => p.get('accountNumber') ?? ''),
    distinctUntilChanged(),
    switchMap((num) =>
      num
        ? this.accountService.getAccount(num).pipe(catchError(() => of(undefined)))
        : of(undefined),
    ),
  );

  protected formatDate(value: string | number[] | undefined): string {
    if (value == null) {
      return '—';
    }
    if (Array.isArray(value) && value.length >= 3) {
      const [y, m, d] = value;
      return `${y}-${String(m).padStart(2, '0')}-${String(d).padStart(2, '0')}`;
    }
    return String(value);
  }

  protected refreshBalance(accountNumber: string): void {
    this.balanceLoading.set(true);
    this.balanceError.set(null);
    this.accountService.getBalance(accountNumber).subscribe({
      next: (b) => {
        this.balanceFromEndpoint.set(b);
        this.balanceLoading.set(false);
      },
      error: () => {
        this.balanceLoading.set(false);
        this.balanceError.set('Could not load balance from the dedicated endpoint.');
      },
    });
  }
}
