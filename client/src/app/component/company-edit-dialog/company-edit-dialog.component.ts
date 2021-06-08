import {Component, Inject, OnInit, Optional, ViewChild} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material/dialog';
import {Company} from '../../model/company.model';
import {AddressEditDialogComponent} from '../address-edit-dialog/address-edit-dialog.component';
import {first} from 'rxjs/operators';
import {CompanyService} from '../../service/company.service';
import {Router} from '@angular/router';
import {Location} from '@angular/common';
import {AuthService} from '../../service/auth.service';

export interface PeriodicElement {
  position: number;
  id?: number;
  city: string;
  address: string;
  active: boolean;
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
  company: Company;

  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort!: MatSort;

  constructor(private dialogRef: MatDialogRef<CompanyEditDialogComponent>,
              private dialog: MatDialog,
              private companyService: CompanyService,
              private router: Router,
              private location: Location,
              private authService: AuthService,
              @Optional() @Inject(MAT_DIALOG_DATA) data: { pageValue: Company }) {
    this.company = data.pageValue;
  }

  ngOnInit(): void {
    if (this.isAdmin()) {
      this.displayedColumns.push('active');
    }
    this.company.addresses.forEach((value, index) => {
      this.periodicElements.push({
        position: index,
        id: value.id,
        city: value.city,
        address: value.address,
        active: value.active
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
    this.dialog.open(AddressEditDialogComponent, {data: this.company});
    this.dialogRef.close();
  }

  onDelete(): void {
    const company: Company = {
      addresses: this.company.addresses,
      city: this.company.city,
      description: this.company.description,
      id: this.company.id,
      name: this.company.name,
      active: false
    };
    this.companyService.delete(company)
      .pipe(first())
      .subscribe(() => {
          this.dialogRef.close();
          this.refreshCompanies();
        }
      );
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  private refreshCompanies(): void {
    this.router.navigateByUrl('/CompaniesComponent', {skipLocationChange: true}).then(() => {
      this.router.navigate([decodeURI(this.location.path())]).then();
    });
  }

  isAuthenticated(): boolean {
    const currentUser = this.authService.currentUserValue;
    return !!currentUser;
  }

  isAdmin(): boolean {
    return this.authService.currentUserValue.role === 'admin';
  }
}
