export interface User {
  id?: number;
  name: string;
  secondName: string;
  email: string;
  password?: string;
  role: string;
  authdata?: string;
  active?: boolean;
}
