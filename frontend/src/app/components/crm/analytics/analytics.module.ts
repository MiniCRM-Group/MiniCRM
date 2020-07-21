import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AnalyticsComponent } from './analytics.component';
import {MatTabsModule} from '@angular/material/tabs';
@NgModule({
  declarations: [AnalyticsComponent],
  exports: [AnalyticsComponent],
  imports: [
    CommonModule,
    MatTabsModule
  ]
  exports: [
  MatTabsModule
  ]
})
export class AnalyticsModule { }
