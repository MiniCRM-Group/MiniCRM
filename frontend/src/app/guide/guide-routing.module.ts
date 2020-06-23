import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { GuideInfoComponent } from './guide-info/guide-info.component';

const routes: Routes = [
  {
    path:'',
    component: GuideInfoComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GuideRoutingModule { }
