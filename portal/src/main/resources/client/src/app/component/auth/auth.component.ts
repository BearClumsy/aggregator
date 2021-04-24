import {first} from 'rxjs/operators';
import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthService} from '../../service/auth.service';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css']
})
export class AuthComponent implements OnInit {
  private submitted: boolean | undefined;
  email = new FormControl('', [Validators.required, Validators.email]);
  password = new FormControl();
  private returnUrl = '';
  private registrationPage = '/registration';

  getEmailErrorMessage(): string {
    return this.email.hasError('required') ? 'You must enter a value' :
      this.email.hasError('email') ? 'Not a valid email' :
        '';
  }

  getPasswordErrorMessage(): string {
    return this.email.hasError('required') ? 'You must enter a value' :
      this.email.hasError('password') ? 'Not a valid password' :
        '';
  }

  constructor(
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar,
    private formBuilder: FormBuilder,
    private route: ActivatedRoute
  ) {
    if (this.authService.currentUser) {
      this.router.navigate(['/']).then();
    }
  }

  ngOnInit(): void {
    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams[this.returnUrl] || '/';
  }

  onSubmit(): void {
    this.submitted = true;

    if (!this.email.value) {
      return;
    }

    if (!this.password.value) {
      return;
    }

    this.authService.login(this.email.value, this.password.value)
      .pipe(first())
      .subscribe(
        data => {
          this.router.navigate([this.returnUrl]).then();
        });
  }

  toRegistration(): void {
    this.router.navigate([this.registrationPage]).then();
  }

}
