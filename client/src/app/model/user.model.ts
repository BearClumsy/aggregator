export interface User {
  id?: number;
  firstName: string;
  secondName: string;
  email: string;
  password?: string;
  login: string;
  role: string;
  authdata?: string;
  active: boolean;
}
