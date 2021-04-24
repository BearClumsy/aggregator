import {Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {MatDialog} from '@angular/material/dialog';
import {CompanyService} from '../../service/company.service';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {map} from 'rxjs/operators';

export interface PeriodicElement {
  position: number;
  id: number;
  name: string;
  city: string;
  description: string;
}

@Component({
  selector: 'app-companies',
  templateUrl: './companies.component.html',
  styleUrls: ['./companies.component.css']
})
export class CompaniesComponent implements OnInit {
  displayedColumns: string[] = ['position', 'name', 'city', 'description'];
  private data: PeriodicElement[] = [];
  dataSource = new MatTableDataSource<PeriodicElement>(this.data);
  private pageSize = 10;

  // @ts-ignore
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  // @ts-ignore
  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor(private http: HttpClient,
              private dialog: MatDialog,
              private service: CompanyService) {
  }

  ngOnInit(): void {
    this.service.getCompanies().pipe(map(res => {
      const periodicElements: PeriodicElement[] = [];
      res.forEach((value, index) => {
        periodicElements.push({
          position: index,
          id: value.id,
          name: value.name,
          city: value.city,
          description: value.description
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

  applyFilter(filterValue: string): void {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  createCompany(): void {
    // this.dialog.open(NewAddressBookComponent, {disableClose: false});
  }

  selected(row?: PeriodicElement): void {
    /*const addressBook: AddressBookModel = {
      id: row.id,
      fullName: row.fullName,
      email: row.email,
    };

    this.dialog.open(AddressBookEditDialogComponent, {
      disableClose: false,
      data: {pageValue: addressBook}
    });*/
  }
}
