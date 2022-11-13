import {Component, Inject, OnInit, Optional, ViewChild} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material/dialog';
import {ScannerConfigsService} from '../../service/scanner-configs.service';
import {Router} from '@angular/router';
import {AuthService} from '../../service/auth.service';
import {ScannerConfig} from '../../model/scanner-configs.model';
import {FormControl} from '@angular/forms';
import {MatTableDataSource} from '@angular/material/table';
import {PeriodicElement} from '../new-scanner-configs-dialog/new-scanner-configs-dialog.component';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {first} from 'rxjs/operators';

@Component({
  selector: 'app-scanner-configs-edit-dialog',
  templateUrl: './scanner-configs-edit-dialog.component.html',
  styleUrls: ['./scanner-configs-edit-dialog.component.css']
})
export class ScannerConfigsEditDialogComponent implements OnInit {
  config: ScannerConfig;
  displayedColumns: string[] = ['tag', 'type', 'action', 'value', 'delete'];
  name = new FormControl();
  url = new FormControl();
  tag = new FormControl();
  type = new FormControl();
  action = new FormControl();
  value = new FormControl();
  private id?: number;
  dataSource!: MatTableDataSource<PeriodicElement>;
  private pageSize = 5;

  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort!: MatSort;

  constructor(private dialogRef: MatDialogRef<ScannerConfigsEditDialogComponent>,
              private dialog: MatDialog,
              private service: ScannerConfigsService,
              private router: Router,
              // private location: Location,
              private authService: AuthService,
              private scannerConfigsService: ScannerConfigsService,
              @Optional() @Inject(MAT_DIALOG_DATA) data: { pageValue: ScannerConfig }) {
    this.config = data.pageValue;
  }

  ngOnInit(): void {
    this.id = this.config.id;
    this.name.setValue(this.config.name);
    this.url.setValue(this.config.url);
    const periodicElements: PeriodicElement[] = [];
    this.config.scannerSteps.forEach(value => {
      periodicElements.push({
        tag: value.tag,
        type: value.type,
        action: value.action,
        value: value.value,
        active: value.active
      });
    });
    this.dataSource = new MatTableDataSource<PeriodicElement>(periodicElements);
    this.paginator._changePageSize(this.pageSize);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  onSubmit(): void {
    const newSteps = this.dataSource.data;
    const scannerConfigs: ScannerConfig = {
      id: this.id,
      name: this.name.value,
      url: this.url.value,
      scannerSteps: newSteps,
      active: true,
      userId: this.authService.currentUserValue.id
    };
    this.scannerConfigsService.update(scannerConfigs)
    .pipe(first())
    .subscribe(
      data => {
        this.dialogRef.close();
      }
    );
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
    const tmpData = this.dataSource.data;
    tmpData.push({
      id: this.id,
      tag: this.tag.value,
      type: this.type.value,
      action: this.action.value,
      value: this.value.value,
      active: true
    } as PeriodicElement);
    this.dataSource.data = tmpData;
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

}
