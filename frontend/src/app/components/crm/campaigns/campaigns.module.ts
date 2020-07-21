import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CampaignsComponent } from './campaigns.component';

@NgModule({
  exports: [CampaignsComponent],
  declarations: [CampaignsComponent],
  imports: [
    CommonModule,
    ]
})
export class CampaignsModule { }
