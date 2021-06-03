import { Component, OnInit } from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import {AuthService} from '../../service/auth.service';
import {Router} from '@angular/router';
import {User} from '../../model/user.model';
import {first} from 'rxjs/operators';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {
  private loading: boolean | undefined;
  private submitted: boolean | undefined;
  firstName = new FormControl();
  secondName = new FormControl();
  email = new FormControl('', [Validators.required, Validators.email]);
  password = new FormControl();
  login = new FormControl();
  role = new FormControl();
  active = new FormControl();
  private error = '';
  private loginUrl = '/login';

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
  }

  onSubmit(): void {
    this.submitted = true;

    if (!this.firstName.value) {
      return;
    }
    if (!this.secondName.value) {
      return;
    }
    if (!this.email.value) {
      return;
    }
    if (!this.password.value) {
      return;
    }
    if (!this.login.value){
      return;
    }

    this.loading = true;
    const user: User = {
      firstName: this.firstName.value,
      secondName: this.secondName.value,
      email: this.email.value,
      password: this.password.value,
      login: this.login.value,
      role: 'participant',
      active: true
    };
    this.authService.registration(user)
      .pipe(first())
      .subscribe(
        data => {
          this.router.navigate([this.loginUrl]).then();
        }, error => {
          this.error = error;
          this.loading = false;
        }
      );
  }

  getFirstNameErrorMessage(): string {
    return this.firstName.hasError('required') ? 'You must enter a value' :
      this.firstName.hasError('first_name') ? 'Not a valid first name' :
        '';
  }

  getSecondNameErrorMessage(): string {
    return this.secondName.hasError('required') ? 'You must enter a value' :
      this.secondName.hasError('second_name') ? 'Not a valid second name' :
        '';
  }

  getEmailErrorMessage(): string {
    return this.email.hasError('required') ? 'You must enter a value' :
      this.email.hasError('email') ? 'Not a valid email' :
        '';
  }

  getLoginErrorMessage(): string {
    return this.login.hasError('required') ? 'You must enter a value' :
      this.login.hasError('login') ? 'Not a valid login' :
        '';
  }

  getPasswordErrorMessage(): string {
    return this.password.hasError('required') ? 'You must enter a value' :
      this.password.hasError('password') ? 'Not a valid password' :
        '';
  }

}
