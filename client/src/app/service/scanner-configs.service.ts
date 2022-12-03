import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ScannerConfig} from '../model/scanner-configs.model';

@Injectable({
  providedIn: 'root'
})
export class ScannerConfigsService {

  constructor(private http: HttpClient) {
  }

  getConfigs(): Observable<ScannerConfig[]> {
    return this.http.get<ScannerConfig[]>('/api/scanner-configs');
  }

  create(scannerConfigs: ScannerConfig): Observable<ScannerConfig> {
    return this.http.post<ScannerConfig>('/api/scanner-configs', scannerConfigs);
  }

  delete(scannerConfigs: ScannerConfig): Observable<ScannerConfig> {
    return this.http.patch<ScannerConfig>('/api/scanner-configs', scannerConfigs);
  }

  update(scannerConfigs: ScannerConfig): Observable<ScannerConfig> {
    return this.http.put<ScannerConfig>('/api/scanner-configs', scannerConfigs);
  }

  start(scannerConfigs: ScannerConfig): Observable<ScannerConfig> {
    return this.http.post<ScannerConfig>('/api/scanner-configs/start', scannerConfigs);
  }

  stop(scannerConfig: ScannerConfig): Observable<ScannerConfig> {
    return this.http.post<ScannerConfig>('/api/scanner-configs/stop', scannerConfig);
  }

  checkStatus(): Observable<ScannerConfig[]> {
    return this.http.get<ScannerConfig[]>('/api/scanner-configs/check');
  }
}
