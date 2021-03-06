import {Component, Inject, OnInit, Optional, ViewChild} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {FormControl} from '@angular/forms';
import {MatTableDataSource} from '@angular/material/table';
import {Company} from '../../model/company.model';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {first} from 'rxjs/operators';
import {Address} from '../../model/address.model';
import {CompanyService} from '../../service/company.service';

export interface PeriodicElement {
  city: string;
  address: string;
}

@Component({
  selector: 'app-address-edit-dialog',
  templateUrl: './address-edit-dialog.component.html',
  styleUrls: ['./address-edit-dialog.component.css']
})
export class AddressEditDialogComponent implements OnInit {
  displayedColumns: string[] = ['city', 'address', 'action'];
  name = new FormControl();
  description = new FormControl();
  city = new FormControl();
  address = new FormControl();
  dataSource!: MatTableDataSource<PeriodicElement>;
  private readonly data;
  private pageSize = 5;
  private id?: number;

  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort!: MatSort;

  constructor(private dialogRef: MatDialogRef<AddressEditDialogComponent>,
              private companyService: CompanyService,
              @Optional() @Inject(MAT_DIALOG_DATA) data: Company) {
    this.data = data;
  }

  ngOnInit(): void {
    this.id = this.data.id;
    this.name.setValue(this.data.name);
    this.description.setValue(this.data.description);
    const periodicElements: PeriodicElement[] = [];
    this.data.addresses.forEach((value, index) => {
      periodicElements.push({
        city: value.city,
        address: value.address
      });
    });
    this.dataSource = new MatTableDataSource<PeriodicElement>(periodicElements);
    this.paginator._changePageSize(this.pageSize);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  onSubmit(): void {
    if (!this.name.value) {
      return;
    }
    if (!this.description.value) {
      return;
    }
    if (!this.city.value) {
      return;
    }
    if (!this.address.value) {
      return;
    }
    const newAddresses: Address[] = [];
    this.dataSource.data.forEach((value, index) => {
      if (index > 0) {
        newAddresses[index - 1] = {
          city: value.city,
          address: value.address
        };
      }
    });
    newAddresses[newAddresses.length] = {
      city: this.city.value,
      address: this.address.value
    };
    const company: Company = {
      id: this.id,
      name: this.name.value,
      city: this.city.value,
      description: this.description.value,
      addresses: newAddresses
    };
    this.companyService.update(company)
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

  onCancel(): void {
    this.dialogRef.close();
  }

  getDescriptionErrorMessage(): string {
    return this.description.hasError('required') ? 'You must enter a value' :
      this.description.hasError('description') ? 'Not a valid description' :
        '';
  }

  getCityErrorMessage(): string {
    return this.city.hasError('required') ? 'You must enter a value' :
      this.city.hasError('city') ? 'Not a valid city' :
        '';
  }

  getAddressErrorMessage(): string {
    return this.address.hasError('required') ? 'You must enter a value' :
      this.address.hasError('address') ? 'Not a valid address' :
        '';
  }

  delete(row: PeriodicElement): void {
    const index = this.dataSource.data.indexOf(row);
    this.dataSource.data.splice(index, 1);
    this.dataSource._updateChangeSubscription();
  }

  addNewAddress(): void {
    const tmpData = this.dataSource.data;
    tmpData.push({
      city: this.city.value,
      address: this.address.value
    });
    this.dataSource.data = tmpData;
  }
}
