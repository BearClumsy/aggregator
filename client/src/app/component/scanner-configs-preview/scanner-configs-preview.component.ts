import {Component, Inject, OnInit, Optional, ViewChild} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {ScannerConfigsService} from "../../service/scanner-configs.service";
import {Router} from "@angular/router";
import {AuthService} from "../../service/auth.service";
import {ScannerConfig} from "../../model/scanner-configs.model";
import {map, tap} from "rxjs/operators";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";

export interface PeriodicElement {
  position: number;
  id?: number;
  startDate: string;
  finishDate: string;
  value: string;
}

@Component({
  selector: 'app-scanner-configs-preview',
  templateUrl: './scanner-configs-preview.component.html',
  styleUrls: ['./scanner-configs-preview.component.css']
})
export class ScannerConfigsPreviewComponent implements OnInit {
  displayedColumns: string[] = ['position', 'startDate', 'finishDate', 'value'];
  private periodicElements: PeriodicElement[] = [];
  dataSource = new MatTableDataSource<PeriodicElement>(this.periodicElements);
  private pageSize = 5;

  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort!: MatSort;
  private data: ScannerConfig;

  constructor(private dialogRef: MatDialogRef<ScannerConfigsPreviewComponent>,
              private dialog: MatDialog,
              private service: ScannerConfigsService,
              private router: Router,
              // private location: Location,
              private authService: AuthService,
              private scannerConfigsService: ScannerConfigsService,
              @Optional() @Inject(MAT_DIALOG_DATA) data: { pageValue: ScannerConfig }) {
    this.data = data.pageValue;
  }

  ngOnInit(): void {
    this.service.getPreview(this.data.id)
    .pipe(map(res => {
      const periodicElements: PeriodicElement[] = [];
      res.forEach((value, index) => {
        periodicElements.push({
          position: index,
          startDate: value.startDate,
          finishDate: value.finishDate,
          value: value.value.substr(0, 100) + "..."
        });
      });
      return periodicElements;
    })).subscribe(value => {
      this.paginator._changePageSize(this.pageSize);
      this.dataSource = new MatTableDataSource<PeriodicElement>(value);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    });
  }

  getScannerConfigName(): string {
    return this.data.name
  }

  downloadCsv(): void {
    this.service.downloadFile("csv", this.data.id).pipe(tap((file: Blob) => {
      const anchor = document.createElement('a');
      anchor.href = URL.createObjectURL(file);
      anchor.download = `report.csv`;
      anchor.click();
    }),).subscribe();
  }

  applyFilter(filterValue: any): void {
    this.dataSource.filter = filterValue.target.value.trim().toLowerCase();
  }
}
