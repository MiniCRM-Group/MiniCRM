import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AnalyticsComponent } from './analytics.component';
import {MatTabsModule} from '@angular/material/tabs';
import { MapComponent } from './map/map.component';
import { ChartsComponent } from './charts/charts.component';
@NgModule({
  declarations: [AnalyticsComponent, MapComponent, ChartsComponent],
  exports: [AnalyticsComponent, MatTabsModule],
  imports: [
    CommonModule,
    MatTabsModule
  ]
})
export class AnalyticsModule { }
