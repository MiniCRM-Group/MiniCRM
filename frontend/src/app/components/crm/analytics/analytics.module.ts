import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AnalyticsComponent } from './analytics.component';
import { MapComponent } from './map/map.component';
import { MatTabsModule } from '@angular/material/tabs';
import { GoogleMapsModule } from '@angular/google-maps';
import { ChartsModule } from './charts/charts.module';
import { MatButtonModule } from '@angular/material/button';

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
    MatButtonModule,
    GoogleMapsModule,
    ChartsModule
  ]
})
export class AnalyticsModule { }
