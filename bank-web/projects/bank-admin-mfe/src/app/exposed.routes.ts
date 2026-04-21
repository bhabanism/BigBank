import type { Routes } from '@angular/router';

export const ADMIN_REMOTE_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./admin/admin').then((m) => m.Admin),
  },
];
