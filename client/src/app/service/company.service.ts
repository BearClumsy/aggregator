import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Company} from '../model/company.model';

@Injectable({
  providedIn: 'root'
})
export class CompanyService {

  constructor(private http: HttpClient) {
  }

  getCompanies(): Observable<Company[]> {
    return this.http.get<Company[]>('/api/company');
  }

  create(company: Company): Observable<Company> {
    return this.http.post<Company>('/api/company', company);
  }

  update(company: Company): Observable<Company> {
    return this.http.put<Company>('/api/company', company);
  }
}
