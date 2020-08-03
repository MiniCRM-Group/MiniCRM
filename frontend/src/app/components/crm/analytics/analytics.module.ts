import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AnalyticsComponent } from './analytics.component';
import { MapComponent } from './map/map.component';
import { ChartsComponent } from './charts/charts.component';
import { MatTabsModule } from '@angular/material/tabs';
// Material imports
import { MatButtonModule } from '@angular/material/button';
// Google Maps type imports
import {} from 'googlemaps';

@NgModule({
  declarations: [AnalyticsComponent, MapComponent, ChartsComponent ],
  exports: [AnalyticsComponent, MatTabsModule,],
  imports: [
    CommonModule,
    MatTabsModule,
    MatButtonModule
  ]
})
export class AnalyticsModule { }
