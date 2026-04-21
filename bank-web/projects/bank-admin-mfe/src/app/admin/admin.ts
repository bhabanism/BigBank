import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { AdminService } from '../services/admin-service';

@Component({
  selector: 'app-admin',
  imports: [ReactiveFormsModule],
  templateUrl: './admin.html',
  styleUrl: './admin.css',
})
export class Admin {
  private readonly adminService = inject(AdminService);
  private readonly fb = inject(FormBuilder);

  protected readonly result = signal<string | null>(null);
  protected readonly error = signal<string | null>(null);
  protected readonly loading = signal(false);

  protected readonly registerResult = signal<{ customerId: number; username: string } | null>(null);
  protected readonly registerError = signal<string | null>(null);
  protected readonly registerSubmitting = signal(false);

  readonly registerForm = this.fb.nonNullable.group({
    username: ['', Validators.required],
    password: ['', [Validators.required, Validators.minLength(8)]],
    email: ['', [Validators.required, Validators.email]],
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
  });

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

  protected submitRegister(): void {
    this.registerResult.set(null);
    this.registerError.set(null);
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }
    this.registerSubmitting.set(true);
    this.adminService.registerUser(this.registerForm.getRawValue()).subscribe({
      next: (res) => {
        this.registerSubmitting.set(false);
        this.registerResult.set(res);
        this.registerForm.reset();
      },
      error: (err) => {
        this.registerSubmitting.set(false);
        const status = err?.status;
        if (status === 403) {
          this.registerError.set('Forbidden: admin role required.');
        } else if (status === 409) {
          this.registerError.set(err.error?.message ?? 'Email or username already in use.');
        } else if (status === 400) {
          this.registerError.set('Invalid request. Check all fields.');
        } else {
          this.registerError.set('Registration failed. Is the API running?');
        }
      },
    });
  }
}
