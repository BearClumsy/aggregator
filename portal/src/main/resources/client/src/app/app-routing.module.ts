import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {CompaniesComponent} from "./component/companies/companies.component";

const routes: Routes = [
  {path: '', component: CompaniesComponent},
  {path: 'companies', component: CompaniesComponent},

  // otherwise redirect to home
  {path: '**', redirectTo: ''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {enableTracing: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
