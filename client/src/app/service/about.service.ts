import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {About} from "../model/about.model";

@Injectable({
  providedIn: 'root'
})
export class AboutService {
  private url = '/actuator/info';

  constructor(private http: HttpClient) { }

  getInfo(): Observable<About> {
    return this.http.get<About>(this.url);
  }
}
