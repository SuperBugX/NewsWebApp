import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomapageComponent } from './components/homapage/homapage.component';

const routes: Routes = [
  { path: '', component: HomapageComponent },
  { path: 'homepage', component: HomapageComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
