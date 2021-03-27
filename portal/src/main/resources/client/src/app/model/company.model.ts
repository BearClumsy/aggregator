import {Address} from "./address.model";

export interface Company {
  id: number;
  name: string;
  city: string;
  description: string;
  addresses: Address[];
}
