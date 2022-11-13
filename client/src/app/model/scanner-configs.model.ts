import {ScannerSteps} from './scanner-steps.model';

export interface ScannerConfig {
  id?: number;
  userId?: number;
  name: string;
  url: string;
  scannerSteps: ScannerSteps[];
  active: boolean;
}
