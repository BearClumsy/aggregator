import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {CompaniesComponent} from './component/companies/companies.component';
import {AuthComponent} from './component/auth/auth.component';
import {RegistrationComponent} from './component/registration/registration.component';
import {AuthGuardService} from './helper/auth-guard.service';

const routes: Routes = [
  {path: '', component: CompaniesComponent, canActivate: [AuthGuardService]},
  {path: 'companies', component: CompaniesComponent, canActivate: [AuthGuardService]},
  {path: 'login', component: AuthComponent},
  {path: 'registration', component: RegistrationComponent},

  // otherwise redirect to home
  {path: '**', redirectTo: ''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {enableTracing: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
