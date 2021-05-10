import {Component, Inject, OnInit, Optional, ViewChild} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material/dialog';
import {Company} from '../../model/company.model';
import {AddressEditDialogComponent} from '../address-edit-dialog/address-edit-dialog.component';

export interface PeriodicElement {
  position: number;
  id?: number;
  city: string;
  address: string;
}

@Component({
  selector: 'app-company-edit-dialog',
  templateUrl: './company-edit-dialog.component.html',
  styleUrls: ['./company-edit-dialog.component.css']
})
export class CompanyEditDialogComponent implements OnInit {
  displayedColumns: string[] = ['position', 'city', 'address'];
  private periodicElements: PeriodicElement[] = [];
  dataSource = new MatTableDataSource<PeriodicElement>(this.periodicElements);
  private pageSize = 10;
  private data;

  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort!: MatSort;

  constructor(private dialogRef: MatDialogRef<CompanyEditDialogComponent>,
              private dialog: MatDialog,
              @Optional() @Inject(MAT_DIALOG_DATA) data: { pageValue: Company }) {
    this.data = data;
  }

  ngOnInit(): void {
    this.data.pageValue.addresses.forEach((value, index) => {
      this.periodicElements.push({
        position: index,
        id: value.id,
        city: value.city,
        address: value.address
      });
    });

    this.paginator._changePageSize(this.pageSize);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  applyFilter(filterValue: any): void {
    this.dataSource.filter = filterValue.target.value.trim().toLowerCase();
  }

  selected(row: PeriodicElement): void {
    const city = row.city;
    const address = row.address;
    window.open('https://www.google.com.ua/maps/search/' + city + ',' + address, '_blank');
  }

  onEdit(): void {
    this.dialog.open(AddressEditDialogComponent, {data: this.data.pageValue});
    this.dialogRef.close();
  }

  onDelete(): void {
    this.dialogRef.close();
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
