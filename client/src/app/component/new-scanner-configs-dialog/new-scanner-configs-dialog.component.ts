import {Component, OnInit, ViewChild} from '@angular/core';
import {FormControl} from '@angular/forms';
import {MatTableDataSource} from '@angular/material/table';
import {MatDialogRef} from '@angular/material/dialog';
import {Router} from '@angular/router';
import {Location} from '@angular/common';
import {first} from 'rxjs/operators';
import {ScannerSteps} from '../../model/scanner-steps.model';
import {ScannerConfig} from '../../model/scanner-configs.model';
import {ScannerConfigsService} from '../../service/scanner-configs.service';
import {AuthService} from '../../service/auth.service';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';

export interface PeriodicElement {
  tag: string;
  type: string;
  action: string;
  value: string;
  active: boolean;
}

@Component({
  selector: 'app-new-scanner-configs-dialog',
  templateUrl: './new-scanner-configs-dialog.component.html',
  styleUrls: ['./new-scanner-configs-dialog.component.css']
})
export class NewScannerConfigsDialogComponent implements OnInit {
  displayedColumns: string[] = ['tag', 'type', 'action', 'value', 'delete'];
  name = new FormControl();
  url = new FormControl();
  tag = new FormControl();
  type = new FormControl();
  action = new FormControl();
  value = new FormControl();
  private periodicElements: PeriodicElement[] = [];
  dataSource = new MatTableDataSource<PeriodicElement>(this.periodicElements);
  private pageSize = 5;

  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort!: MatSort;

  constructor(private dialogRef: MatDialogRef<NewScannerConfigsDialogComponent>,
              private router: Router,
              private location: Location,
              private authService: AuthService,
              private scannerConfigsService: ScannerConfigsService) {
  }

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource<PeriodicElement>(this.periodicElements);
    this.periodicElements.push({
      tag: '',
      type: '',
      action: '',
      value: '',
      active: true
    });
  }

  onSubmit(): void {
    if (!this.tag.value) {
      return;
    }
    if (!this.type.value) {
      return;
    }
    if (!this.action.value) {
      return;
    }
    const newSteps: ScannerSteps[] = [];
    this.periodicElements.forEach((value, index) => {
      if (index > 0) {
        newSteps[index - 1] = {
          tag: value.tag,
          type: value.type,
          action: value.action,
          value: value.value,
          active: value.active
        };
      }
    });
    newSteps[newSteps.length] = {
      tag: this.tag.value,
      type: this.type.value,
      action: this.action.value,
      value: this.value.value,
      active: true
    };
    const scannerConfigs: ScannerConfig = {
      name: this.name.value,
      url: this.url.value,
      scannerSteps: newSteps,
      active: true,
      userId: this.authService.currentUserValue.id
    };
    this.scannerConfigsService.create(scannerConfigs)
    .pipe(first())
    .subscribe(
      data => {
        this.dialogRef.close();
        this.refreshScannerConfigs();
      }
    );
    this.paginator._changePageSize(this.pageSize);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  getNameErrorMessage(): string {
    return this.name.hasError('required') ? 'You must enter a value' :
      this.name.hasError('name') ? 'Not a valid name' :
        '';
  }

  getUrlErrorMessage(): string {
    return this.url.hasError('required') ? 'You must enter a value' :
      this.url.hasError('url') ? 'Not a valid url' :
        '';
  }

  getTypeErrorMessage(): string {
    return this.type.hasError('required') ? 'You must enter a value' :
      this.type.hasError('type') ? 'Not a valid type' :
        '';
  }

  getTagErrorMessage(): string {
    return this.tag.hasError('required') ? 'You must enter a value' :
      this.tag.hasError('tag') ? 'Not a valid tag' :
        '';
  }

  getActionErrorMessage(): string {
    return this.action.hasError('required') ? 'You must enter a value' :
      this.action.hasError('action') ? 'Not a valid action' :
        '';
  }

  addNewStep(): void {
    this.dataSource = new MatTableDataSource<PeriodicElement>(this.periodicElements);
    this.periodicElements.push({
      tag: this.tag.value,
      type: this.type.value,
      action: this.action.value,
      value: this.value.value,
      active: true
    });
    this.paginator._changePageSize(this.pageSize);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  delete(row: PeriodicElement): void {
    const index = this.dataSource.data.indexOf(row);
    this.dataSource.data.splice(index, 1);
    this.dataSource._updateChangeSubscription();
  }

  private refreshScannerConfigs(): void {
    this.router.navigateByUrl('/ScannerConfigsComponent', {skipLocationChange: true}).then(() => {
      this.router.navigate([decodeURI(this.location.path())]).then();
    });
  }
}
