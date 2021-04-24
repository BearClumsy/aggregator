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
  private fullName = new FormControl();
  private email = new FormControl('', [Validators.required, Validators.email]);
  private secondName = new FormControl();
  private password = new FormControl();
  private error = '';
  private loginUrl = '/login';

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
  }

  onSubmit(): void {
    this.submitted = true;

    if (!this.fullName.value) {
      return;
    }

    if (!this.email.value) {
      return;
    }

    if (!this.secondName.value) {
      return;
    }

    if (!this.password.value) {
      return;
    }

    this.loading = true;
    const user: User = {
      name: this.fullName.value,
      email: this.email.value,
      secondName: this.secondName.value,
      password: this.password.value,
      role: 'PARTICIPANT'
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

  getFullNameErrorMessage(): string {
    return this.email.hasError('required') ? 'You must enter a value' :
      this.email.hasError('full_name') ? 'Not a valid full name' :
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

}
