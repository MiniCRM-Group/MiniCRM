import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AnalyticsComponent } from './analytics.component';
import {MatTabsModule} from '@angular/material/tabs';
import { GoogleMapsModule } from '@angular/google-maps';
@NgModule({
  declarations: [AnalyticsComponent],
  exports: [AnalyticsComponent, MatTabsModule],
  imports: [
    CommonModule,
    MatTabsModule,
    GoogleMapsModule
  ]
})
export class AnalyticsModule { }
