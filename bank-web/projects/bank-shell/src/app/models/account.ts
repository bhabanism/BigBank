export interface Account {
  id?: number;
  accountNumber: string;
  accountType: string;
  balance: number;
  openDate?: string | number[];
  status: string;
  customerName: string;
  customerEmail: string;
  customerPhone: string;
}
