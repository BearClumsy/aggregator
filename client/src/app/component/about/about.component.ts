import {Component, OnInit} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {AboutService} from '../../service/about.service';
import {About} from '../../model/about.model';

@Component({
  selector: 'app-about',
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.css']
})
export class AboutComponent implements OnInit {
  name: any;
  version: any;
  buildDate: any;
  author: any;

  constructor(public dialogRef: MatDialogRef<AboutComponent>, private service: AboutService) {
  }

  ngOnInit(): void {
    this.service.getInfo().subscribe((res: About) => {
      this.name = res.build.name;
      this.version = res.build.version;
      this.buildDate = res.build.time;
      this.author = res.info.author;
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
