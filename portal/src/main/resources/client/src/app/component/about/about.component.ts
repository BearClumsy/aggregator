import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {MatDialogRef} from '@angular/material/dialog';
import {Observable} from 'rxjs';

export interface Info {
  name: string;
  version: string;
  buildDate: string;
}

@Component({
  selector: 'app-about',
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.css']
})
export class AboutComponent implements OnInit {
  url = '/actuator/info';
  name: any;
  version: any;
  buildDate: any;
  author: any;

  constructor(private http: HttpClient, public dialogRef: MatDialogRef<AboutComponent>) {
  }

  ngOnInit(): void {
    this.getInfo().subscribe(res => {
      console.log(res);
      this.name = res.build.name;
      this.version = res.build.version;
      this.buildDate = res.build.time;
      this.author = res.info.author;
    });
  }

  getInfo(): Observable<any> {
    return this.http.get<Info>(this.url);
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
