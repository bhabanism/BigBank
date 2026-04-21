import { Routes } from '@angular/router';
import { loadRemoteModule } from '@angular-architects/module-federation';

import { authGuard, guestGuard } from './core/auth.guard';
import { ADMIN_REMOTE_ENTRY } from './remote-config';

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
        loadChildren: () =>
          loadRemoteModule({
            type: 'module',
            remoteEntry: ADMIN_REMOTE_ENTRY,
            exposedModule: './Routes',
          }).then((m) => m.ADMIN_REMOTE_ROUTES),
      },
    ],
  },
];
