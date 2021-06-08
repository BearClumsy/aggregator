import {Component, Inject, OnInit, Optional} from '@angular/core';
import {User} from '../../model/user.model';
import {FormControl, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {UserService} from '../../service/user.service';
import {Router} from '@angular/router';
import {first} from 'rxjs/operators';
import {Location} from '@angular/common';

@Component({
  selector: 'app-user-edit-dialog',
  templateUrl: './user-edit-dialog.component.html',
  styleUrls: ['./user-edit-dialog.component.css']
})
export class UserEditDialogComponent implements OnInit {
  user: User;
  firstName = new FormControl();
  secondName = new FormControl();
  email = new FormControl('', [Validators.required, Validators.email]);
  login = new FormControl();
  password = new FormControl();
  role = new FormControl();
  active = new FormControl();

  constructor(private dialogRef: MatDialogRef<UserEditDialogComponent>,
              private userService: UserService,
              private router: Router,
              private location: Location,
              @Optional() @Inject(MAT_DIALOG_DATA) private data: {pageValue: User}
  ) {
    this.user = data.pageValue;
    this.firstName.setValue(this.user.firstName);
    this.secondName.setValue(this.user.secondName);
    this.email.setValue(this.user.email);
    this.login.setValue(this.user.login);
    this.password.setValue(this.user.password);
    this.role.setValue(this.user.role);
    this.active.setValue(this.user.active);
  }

  onSubmit(): void {
    if (!this.firstName.value) {
      return;
    }
    if (!this.secondName.value) {
      return;
    }
    if (!this.email.value) {
      return;
    }
    if (!this.login.value) {
      return;
    }
    if (!this.password.value) {
      return;
    }
    if (!this.role.value) {
      return;
    }
    if (!this.active.value) {
      return;
    }

    const user: User = {
      id: this.user.id,
      firstName: this.firstName.value,
      secondName: this.secondName.value,
      email: this.email.value,
      login: this.login.value,
      password: this.password.value,
      role: this.role.value,
      active: this.active.value
    };
    this.userService.update(user)
      .pipe(first())
      .subscribe(
        data => {
          this.dialogRef.close();
          this.refreshUsers();
        }
      );
  }

  getFirstNameErrorMessage(): string {
    return this.email.hasError('required') ? 'You must enter a value' :
      this.email.hasError('first_name') ? 'Not a valid first name' :
        '';
  }

  getSecondNameErrorMessage(): string {
    return this.email.hasError('required') ? 'You must enter a value' :
      this.email.hasError('second_name') ? 'Not a valid second name' :
        '';
  }

  getEmailErrorMessage(): string {
    return this.email.hasError('required') ? 'You must enter a value' :
      this.email.hasError('email') ? 'Not a valid email' :
        '';
  }

  getLoginErrorMessage(): string {
    return this.email.hasError('required') ? 'You must enter a value' :
      this.email.hasError('login') ? 'Not a valid login' :
        '';
  }

  getPasswordErrorMessage(): string {
    return this.email.hasError('required') ? 'You must enter a value' :
      this.email.hasError('password') ? 'Not a valid password' :
        '';
  }

  getRoleErrorMessage(): string {
    return this.email.hasError('required') ? 'You must enter a value' :
      this.email.hasError('role') ? 'Not a valid role' :
        '';
  }

  getActiveErrorMessage(): string {
    return this.email.hasError('required') ? 'You must enter a value' :
      this.email.hasError('active') ? 'Not a valid active' :
        '';
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  delete(): void {
    this.userService.delete(this.user.id);
    this.dialogRef.close();
    this.refreshUsers();
  }

  refreshUsers(): void {
    this.router.navigateByUrl('/UsersComponent', {skipLocationChange: true}).then(() => {
      this.router.navigate([decodeURI(this.location.path())]).then();
    });
  }

  ngOnInit(): void {
  }
}
