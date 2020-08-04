import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AnalyticsComponent } from './analytics.component';
import { MapComponent } from './map/map.component';
import { ChartsComponent } from './charts/charts.component';
import { MatTabsModule } from '@angular/material/tabs';
import { GoogleMapsModule } from '@angular/google-maps';
import { ChartsModule } from './charts/charts.module';

@NgModule({
  declarations: [
    AnalyticsComponent,
    MapComponent
  ],
  exports: [
    AnalyticsComponent
  ],
  imports: [
    CommonModule,
    MatTabsModule,
    GoogleMapsModule,
    ChartsModule
  ]
})
export class AnalyticsModule { }
