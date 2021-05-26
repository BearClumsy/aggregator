import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {NavigationComponent} from './component/navigation/navigation.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {AboutComponent} from './component/about/about.component';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatCardModule} from '@angular/material/card';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatIconModule} from '@angular/material/icon';
import {MatListModule} from '@angular/material/list';
import {FlexModule} from '@angular/flex-layout';
import {MatButtonModule} from '@angular/material/button';
import {MatTooltipModule} from '@angular/material/tooltip';
import {MatDialogModule} from '@angular/material/dialog';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {CompaniesComponent} from './component/companies/companies.component';
import {MatTableModule} from '@angular/material/table';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSortModule} from '@angular/material/sort';
import { AuthComponent } from './component/auth/auth.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { RegistrationComponent } from './component/registration/registration.component';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Overlay} from '@angular/cdk/overlay';
import {LiveAnnouncer} from '@angular/cdk/a11y';
import {BasicAuthInterceptor} from './helper/basic-auth.interceptor';
import {ErrorInterceptor} from './helper/error.interceptor';
import {AuthService} from './service/auth.service';
import {MatInputModule} from '@angular/material/input';
import { UsersComponent } from './component/users/users.component';
import { NewUserDialogComponent } from './component/new-user-dialog/new-user-dialog.component';
import {MatSelectModule} from '@angular/material/select';
import { UserEditDialogComponent } from './component/user-edit-dialog/user-edit-dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    NavigationComponent,
    AboutComponent,
    CompaniesComponent,
    AuthComponent,
    RegistrationComponent,
    UsersComponent,
    NewUserDialogComponent,
    UserEditDialogComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatSidenavModule,
    MatCardModule,
    MatToolbarModule,
    MatIconModule,
    MatListModule,
    FlexModule,
    MatButtonModule,
    MatTooltipModule,
    MatDialogModule,
    HttpClientModule,
    MatTableModule,
    MatPaginatorModule,
    MatFormFieldModule,
    MatInputModule,
    MatSortModule,
    FormsModule,
    ReactiveFormsModule,
    MatSelectModule
  ],
  exports: [MatFormFieldModule, MatInputModule],
  providers: [
    AuthService,
    {provide: MatSnackBar, deps: [Overlay, LiveAnnouncer]},
    {provide: HTTP_INTERCEPTORS, useClass: BasicAuthInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true},
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
