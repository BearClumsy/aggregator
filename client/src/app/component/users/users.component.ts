import {Component, OnInit, ViewChild} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {HttpClient} from '@angular/common/http';
import {MatDialog} from '@angular/material/dialog';
import {UserService} from '../../service/user.service';
import {map} from 'rxjs/operators';
import {NewUserDialogComponent} from '../new-user-dialog/new-user-dialog.component';
import {User} from '../../model/user.model';
import {UserEditDialogComponent} from '../user-edit-dialog/user-edit-dialog.component';

export interface PeriodicElement {
  id?: number;
  position: number;
  firstName: string;
  secondName: string;
  email: string;
  login: string;
  role: string;
  active: boolean;
}

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {
  displayedColumns: string[] = ['position', 'firstName', 'secondName', 'email', 'login', 'role', 'isActive'];
  private data: PeriodicElement[] = [];
  dataSource = new MatTableDataSource<PeriodicElement>(this.data);
  private pageSize = 10;

  @ViewChild(MatPaginator, {static: true})
  paginator!: MatPaginator;
  @ViewChild(MatSort, {static: true})
  sort!: MatSort;

  constructor(private http: HttpClient,
              private dialog: MatDialog,
              private userService: UserService) {
  }

  ngOnInit(): void {
    this.userService.getUsers().pipe(map(res => {
      const periodicElements: PeriodicElement[] = [];
      res.forEach((value, index) => {
        periodicElements.push({
          position: index,
          id: value.id,
          firstName: value.firstName,
          secondName: value.secondName,
          email: value.email,
          login: value.login,
          role: value.role,
          active: value.active
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

  createUser(): void {
    this.dialog.open(NewUserDialogComponent, {disableClose: false});
  }

  selected(row: PeriodicElement): void {
    const user: User = {
      id: row?.id,
      firstName: row.firstName,
      secondName: row.secondName,
      email: row.email,
      login: row.login,
      role: row.role,
      active: row.active
    };

    this.dialog.open(UserEditDialogComponent, {disableClose: false, data: {pageValue: user}});
  }
}
