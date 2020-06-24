import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LeadsListComponent } from './leads-list/leads-list.component';

const routes: Routes = [
  {
    path:'',
    component: LeadsListComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LeadsRoutingModule { }
