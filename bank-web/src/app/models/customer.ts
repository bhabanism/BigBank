export interface Customer {
  id: number;
  firstName: string;
  lastName: string;
  dateOfBirth: string;
  ssn: string;
  address: string;
  city: string;
  state: string;
  zipCode: string;
  phone: string;
  email: string;
  registrationDate?: string | number[];
}
