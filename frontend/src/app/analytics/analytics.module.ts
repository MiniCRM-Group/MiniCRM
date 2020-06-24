import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AnalyticsRoutingModule } from './analytics-routing.module';
import { AnalyticsPanelComponent } from './analytics-panel/analytics-panel.component';


@NgModule({
  declarations: [AnalyticsPanelComponent],
  imports: [
    CommonModule,
    AnalyticsRoutingModule
  ]
})
export class AnalyticsModule { }
