import { Component, inject, signal } from '@angular/core';

import { AdminService } from '../../services/admin-service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.html',
  styleUrl: './admin.css',
})
export class Admin {
  private readonly adminService = inject(AdminService);

  protected readonly result = signal<string | null>(null);
  protected readonly error = signal<string | null>(null);
  protected readonly loading = signal(false);

  protected ping(): void {
    this.result.set(null);
    this.error.set(null);
    this.loading.set(true);
    this.adminService.ping().subscribe({
      next: (text) => {
        this.loading.set(false);
        this.result.set(text);
      },
      error: (err) => {
        this.loading.set(false);
        const status = err?.status;
        if (status === 403) {
          this.error.set('Forbidden: this login does not have the ADMIN role.');
        } else {
          this.error.set('Request failed. Ensure you are signed in and the API is running.');
        }
      },
    });
  }
}
