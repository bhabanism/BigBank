import { Component, inject } from '@angular/core';
import { AsyncPipe, DecimalPipe } from '@angular/common';
import { AccountService } from '../../services/account-service';

const ACCOUNT_STATUS_BADGE_CLASSES: Record<string, string> = {
  Active: 'bg-emerald-600 text-white ring-emerald-100/20',
  Inactive: 'bg-gray-600 text-white ring-gray-100/20',
  Closed: 'bg-red-600 text-white ring-red-100/20',
  Frozen: 'bg-blue-600 text-white ring-blue-100/20',
};

const BADGE_BASE = 'inline-flex items-center rounded-full px-3 py-0.5 text-xs font-semibold uppercase tracking-wide ring-1 ring-inset';

@Component({
  selector: 'app-account',
  imports: [AsyncPipe, DecimalPipe],
  templateUrl: './account.html',
  styleUrl: './account.css',
})
export class Account {
  private accountService = inject(AccountService);
  accounts$ = this.accountService.getAllAccounts();

  accountBadgeClass(status: string): string {
    const tone = ACCOUNT_STATUS_BADGE_CLASSES[status] ?? 'bg-slate-100 text-slate-600 ring-slate-500/20';
    return `${BADGE_BASE} ${tone}`;
  }
}
