import {Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {MatDialog} from '@angular/material/dialog';
import {AuthService} from '../../service/auth.service';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {first, map, switchMap, tap} from 'rxjs/operators';
import {ScannerConfigsService} from '../../service/scanner-configs.service';
import {ScannerConfig} from '../../model/scanner-configs.model';
import {
  NewScannerConfigsDialogComponent
} from '../new-scanner-configs-dialog/new-scanner-configs-dialog.component';
import {ScannerSteps} from '../../model/scanner-steps.model';
import {
  ScannerConfigsEditDialogComponent
} from '../scanner-configs-edit-dialog/scanner-configs-edit-dialog.component';
import {Router} from '@angular/router';
import {Location} from '@angular/common';
import {interval} from "rxjs";

export interface PeriodicElement {
  position: number;
  id?: number;
  name: string;
  url: string;
  queue: ScannerSteps[];
  active: boolean;
  isStarted: boolean;

  isCompleted: boolean
}

@Component({
  selector: 'app-scanner-configs',
  templateUrl: './scanner-configs.component.html',
  styleUrls: ['./scanner-configs.component.css']
})
export class ScannerConfigsComponent implements OnInit {
  displayedColumns: string[] = ['position', 'name', 'url', 'progress', 'start', 'preview', 'edit', 'active'];
  private data: PeriodicElement[] = [];
  dataSource = new MatTableDataSource<PeriodicElement>(this.data);
  private pageSize = 10;

  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort!: MatSort;

  constructor(private http: HttpClient,
              private dialog: MatDialog,
              private authService: AuthService,
              private router: Router,
              private location: Location,
              private service: ScannerConfigsService) {
  }

  ngOnInit(): void {
    this.service.getConfigs().pipe(map(res => {
      const periodicElements: PeriodicElement[] = [];
      res.forEach((value, index) => {
        periodicElements.push({
          position: index,
          id: value.id,
          name: value.name,
          url: value.url,
          queue: value.scannerSteps,
          active: value.active,
          isStarted: false,
          isCompleted: false
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

  applyFilter(filterValue: any): void {
    this.dataSource.filter = filterValue.target.value.trim().toLowerCase();
  }

  createScannerConfigs(): void {
    this.dialog.open(NewScannerConfigsDialogComponent, {disableClose: false});
  }

  isAuthenticated(): boolean {
    const currentUser = this.authService.currentUserValue;
    return !!currentUser;
  }

  start(row: PeriodicElement): void {
    row.isStarted = true;
    const configs: ScannerConfig = {
      id: row?.id,
      url: row.url,
      scannerSteps: row.queue,
      name: row.name,
      active: row.active,
    };
    this.service.start(configs)
    .pipe(switchMap(value => {
      return interval(5000).pipe(switchMap(() => {
        return this.service.checkStatus(configs);
      }), tap((result: boolean) => {
        row.isCompleted = result;
        row.isStarted = !result;
      }))
    }))
    .subscribe(
      data => {
        // this.refreshScannerConfigs();
      }
    );
  }

  stop(row: PeriodicElement): void {
    row.isStarted = false;
    const configs: ScannerConfig = {
      id: row?.id,
      url: row.url,
      scannerSteps: row.queue,
      name: row.name,
      active: row.active
    };
    this.service.stop(configs)
    .pipe(first())
    .subscribe(
      data => {
        // this.refreshScannerConfigs();
      }
    );
  }

  delete(row: PeriodicElement): void {
    const configs: ScannerConfig = {
      id: row?.id,
      url: row.url,
      scannerSteps: row.queue,
      name: row.name,
      active: row.active,
      userId: this.authService.currentUserValue.id
    };
    this.service.delete(configs)
    .pipe(first())
    .subscribe(
      data => {
        this.refreshScannerConfigs();
      }
    );
  }

  private refreshScannerConfigs(): void {
    this.router.navigateByUrl('/ScannerConfigsComponent', {skipLocationChange: true}).then(() => {
      this.router.navigate([decodeURI(this.location.path())]).then();
    });
  }

  preview(row: PeriodicElement): void {
    const configs: ScannerConfig = {
      id: row?.id,
      url: row.url,
      scannerSteps: row.queue,
      name: row.name,
      active: row.active
    };
  }

  edit(row: PeriodicElement): void {
    const configs: ScannerConfig = {
      id: row?.id,
      url: row.url,
      scannerSteps: row.queue,
      name: row.name,
      active: row.active
    };
    this.dialog.open(ScannerConfigsEditDialogComponent, {
      disableClose: false,
      data: {pageValue: configs}
    });
  }

  isStarted(element: PeriodicElement): boolean {
    return element.isStarted;
  }

  isPreviewEnabled(element: PeriodicElement): boolean {
    return element.isCompleted;
  }
}
