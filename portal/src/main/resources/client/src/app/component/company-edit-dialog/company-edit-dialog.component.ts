import {Component, Inject, OnInit, Optional, ViewChild} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {UserService} from '../../service/user.service';
import {Router} from '@angular/router';
import {Location} from '@angular/common';
import {Address} from '../../model/address.model';

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
              private userService: UserService,
              private router: Router,
              private location: Location,
              @Optional() @Inject(MAT_DIALOG_DATA) data: { pageValue: Address[] }) {
    this.data = data;
  }

  ngOnInit(): void {
    this.data.pageValue.forEach((value, index) => {
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

}
