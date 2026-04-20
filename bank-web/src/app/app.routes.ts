import { Routes } from '@angular/router';

import { authGuard, guestGuard } from './core/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    canActivate: [guestGuard],
    loadComponent: () => import('./components/login/login').then((m) => m.Login),
  },
  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () => import('./layout/shell/shell').then((m) => m.Shell),
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'accounts' },
      {
        path: 'accounts',
        loadComponent: () => import('./components/account/account').then((m) => m.Account),
      },
      {
        path: 'accounts/:accountNumber',
        loadComponent: () =>
          import('./components/account-detail/account-detail').then((m) => m.AccountDetail),
      },
      {
        path: 'customers',
        loadComponent: () => import('./components/customers/customers').then((m) => m.Customers),
      },
      {
        path: 'admin',
        loadComponent: () => import('./components/admin/admin').then((m) => m.Admin),
      },
    ],
  },
];
