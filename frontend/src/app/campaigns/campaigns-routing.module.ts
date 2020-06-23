import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CampaignsListComponent } from './ campaigns-list/campaigns-list.component';

const routes: Routes = [
  {
    path:'',
    component: CampaignsListComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CampaignsRoutingModule { }
