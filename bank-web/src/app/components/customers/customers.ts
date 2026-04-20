import { AsyncPipe } from '@angular/common';
import { Component, inject } from '@angular/core';

import { CustomerService } from '../../services/customer-service';

@Component({
  selector: 'app-customers',
  imports: [AsyncPipe],
  templateUrl: './customers.html',
  styleUrl: './customers.css',
})
export class Customers {
  private readonly customerService = inject(CustomerService);
  protected readonly customers$ = this.customerService.getAllCustomers();

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
}
