import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ScannerConfig} from '../model/scanner-configs.model';
import {ScannerPreview} from "../model/scanner-preview.model";
import {PojoString} from "../model/pojo-string.model";

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

  checkStatus(scannerConfig: ScannerConfig): Observable<PojoString> {
    return this.http.get<PojoString>('/api/scanner-configs/check/' + scannerConfig.id);
  }

  getPreview(scannerId: number | undefined): Observable<ScannerPreview[]> {
    return this.http.get<ScannerPreview[]>('/api/scanner-configs/preview/' + scannerId);
  }

  downloadFile(type: string, scannerId: number | undefined): Observable<any> {
    return this.http.get("/api/scanner-configs/create/file/" + type + "/" + scannerId, {
      responseType: 'blob',
    });
  }
}
