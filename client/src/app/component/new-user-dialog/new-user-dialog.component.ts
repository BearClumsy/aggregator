import {Component, OnInit} from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import {MatDialogRef} from '@angular/material/dialog';
import {Router} from '@angular/router';
import {UserService} from '../../service/user.service';
import {User} from '../../model/user.model';
import {first} from 'rxjs/operators';
import {Location} from '@angular/common';

@Component({
  selector: 'app-new-user-dialog',
  templateUrl: './new-user-dialog.component.html',
  styleUrls: ['./new-user-dialog.component.css']
})
export class NewUserDialogComponent implements OnInit {

  firstName = new FormControl();
  secondName = new FormControl();
  email = new FormControl('', [Validators.required, Validators.email]);
  login = new FormControl();
  password = new FormControl();
  role = new FormControl();
  active = new FormControl();

  constructor(private dialogRef: MatDialogRef<NewUserDialogComponent>,
              private router: Router,
              private userService: UserService,
              private location: Location) {
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
      firstName: this.firstName.value,
      secondName: this.secondName.value,
      email: this.email.value,
      login: this.login.value,
      password: this.password.value,
      role: this.role.value,
      active: this.active.value
    };
    this.userService.create(user)
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

  onCancel(): void {
    this.dialogRef.close();
  }

  refreshUsers(): void {
    this.router.navigateByUrl('/UsersComponent', {skipLocationChange: true}).then(() => {
      this.router.navigate([decodeURI(this.location.path())]).then();
    });
  }

  ngOnInit(): void {
  }

}
