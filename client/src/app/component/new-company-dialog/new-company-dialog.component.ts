import {Component, OnInit} from '@angular/core';
import {FormControl} from '@angular/forms';
import {MatDialogRef} from '@angular/material/dialog';
import {MatTableDataSource} from '@angular/material/table';
import {Address} from '../../model/address.model';
import {Company} from '../../model/company.model';
import {CompanyService} from '../../service/company.service';
import {first} from 'rxjs/operators';
import {Router} from '@angular/router';
import {Location} from '@angular/common';

export interface PeriodicElement {
  id: number;
  city: string;
  address: string;
}

@Component({
  selector: 'app-new-company-dialog',
  templateUrl: './new-company-dialog.component.html',
  styleUrls: ['./new-company-dialog.component.css']
})
export class NewCompanyDialogComponent implements OnInit {
  displayedColumns: string[] = ['city', 'address', 'action'];
  name = new FormControl();
  city = new FormControl();
  address = new FormControl();
  description = new FormControl();
  private periodicElements: PeriodicElement[] = [];
  dataSource = new MatTableDataSource<PeriodicElement>(this.periodicElements);

  constructor(private dialogRef: MatDialogRef<NewCompanyDialogComponent>,
              private router: Router,
              private location: Location,
              private companyService: CompanyService) {
  }

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource<PeriodicElement>(this.periodicElements);
    this.periodicElements.push({
      id: 0,
      city: '',
      address: ''
    });
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
    this.periodicElements.forEach((value, index) => {
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
      name: this.name.value,
      city: this.city.value,
      description: this.description.value,
      addresses: newAddresses
    };
    this.companyService.create(company)
      .pipe(first())
      .subscribe(
        data => {
          this.dialogRef.close();
          this.refreshCompanies();
        }
      );
  }

  getNameErrorMessage(): string {
    return this.name.hasError('required') ? 'You must enter a value' :
      this.name.hasError('name') ? 'Not a valid name' :
        '';
  }

  getDescriptionErrorMessage(): string {
    return this.description.hasError('required') ? 'You must enter a value' :
      this.description.hasError('description') ? 'Not a valid description' :
        '';
  }

  getAddressesErrorMessage(): string {
    return this.address.hasError('required') ? 'You must enter a value' :
      this.address.hasError('address') ? 'Not a valid address' :
        '';
  }

  getCityErrorMessage(): string {
    return this.city.hasError('required') ? 'You must enter a value' :
      this.city.hasError('city') ? 'Not a valid city' :
        '';
  }

  addNewAddress(): void {
    this.dataSource = new MatTableDataSource<PeriodicElement>(this.periodicElements);
    this.periodicElements.push({
      id: this.periodicElements.length,
      city: this.city.value,
      address: this.address.value
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  delete(row: PeriodicElement): void {
    const index = this.dataSource.data.indexOf(row);
    this.dataSource.data.splice(index, 1);
    this.dataSource._updateChangeSubscription();
  }

  private refreshCompanies(): void {
    this.router.navigateByUrl('/CompaniesComponent', {skipLocationChange: true}).then(() => {
      this.router.navigate([decodeURI(this.location.path())]).then();
    });
  }
}
