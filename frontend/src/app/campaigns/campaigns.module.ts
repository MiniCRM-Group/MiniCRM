import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CampaignsRoutingModule } from './campaigns-routing.module';
import { CampaignsListComponent } from './campaigns-list/campaigns-list.component';


@NgModule({
  declarations: [CampaignsListComponent],
  imports: [
    CommonModule,
    CampaignsRoutingModule
  ]
})
export class CampaignsModule { }
