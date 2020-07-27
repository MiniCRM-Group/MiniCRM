import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CampaignsComponent } from './campaigns.component';
import { CrmTableComponent } from '../../shared/crm-table/crm-table.component';

@NgModule({
  exports: [CampaignsComponent],
  declarations: [CampaignsComponent, CrmTableComponent],
  imports: [
    CommonModule,
  ]
})
export class CampaignsModule { }
