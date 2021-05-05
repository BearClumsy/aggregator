import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../model/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>('/api/user');
  }

  create(user: User): Observable<User>{
    return this.http.post<User>('/api/user', user);
  }

  delete(id?: number): void {
    this.http.delete<User>('/api/user' + id);
  }

  update(user: User): Observable<User>{
    return this.http.put<User>('/api/user', user);
  }
}
