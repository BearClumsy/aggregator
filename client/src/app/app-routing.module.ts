import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {CompaniesComponent} from './component/companies/companies.component';
import {AuthComponent} from './component/auth/auth.component';
import {RegistrationComponent} from './component/registration/registration.component';
import {AuthGuardService} from './helper/auth-guard.service';
import {UsersComponent} from './component/users/users.component';
import {ScannerConfigsComponent} from './component/scanner-configs/scanner-configs.component';

const routes: Routes = [
  {path: 'scanner-configs', component: ScannerConfigsComponent, canActivate: [AuthGuardService]},
  // {path: 'companies', component: CompaniesComponent, canActivate: [AuthGuardService]},
  {path: 'users', component: UsersComponent, canActivate: [AuthGuardService]},
  {path: 'registration', component: RegistrationComponent},
  {path: 'login', component: AuthComponent},
  {path: '', component: ScannerConfigsComponent, canActivate: [AuthGuardService]},

  // otherwise redirect to home
  {path: '**', redirectTo: ''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {enableTracing: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
