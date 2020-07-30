import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CampaignsComponent } from './campaigns.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { CrmTableModule } from '../../shared/crm-table/crm-table.module';

@NgModule({
  exports: [
    CampaignsComponent
  ],
  declarations: [
    CampaignsComponent
  ],
  imports: [
    CommonModule,
    CrmTableModule,
    FlexLayoutModule
  ]
})
export class CampaignsModule { }
