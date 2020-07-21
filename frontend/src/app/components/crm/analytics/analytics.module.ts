import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AnalyticsComponent } from './analytics.component';
import {MatTabsModule} from '@angular/material/tabs';
@NgModule({
  declarations: [AnalyticsComponent],
  exports: [AnalyticsComponent, MatTabsModule],
  imports: [
    CommonModule,
    MatTabsModule
  ]
})
export class AnalyticsModule { }
