import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AnalyticsComponent } from './analytics.component';

@NgModule({
  declarations: [AnalyticsComponent],
  exports: [AnalyticsComponent],
  imports: [
    CommonModule
  ]
})
export class AnalyticsModule { }
