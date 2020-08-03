import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { FormsModule } from '@angular/forms';

// Flex + Grid Layout
import { FlexLayoutModule } from '@angular/flex-layout';

// Material imports
import { MatButtonModule } from '@angular/material/button';

// Google Maps type imports
import {} from 'googlemaps';

// Routing
import { AppRoutingModule } from './app-routing.module';
import { LocationStrategy, PathLocationStrategy } from '@angular/common';

// Component imports
import { AppComponent } from './app.component';
import { MapComponent } from './components/crm/analytics/map/map.component';
import { ChartsComponent } from './components/crm/analytics/charts/charts.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CrmModule } from './components/crm/crm.module';
import { GuideModule } from './components/crm/guide/guide.module';
import { LeadsModule } from './components/crm/leads/leads.module';
import { FormsCrmModule } from './components/crm/forms/forms.module';
import { CampaignsModule } from './components/crm/campaigns/campaigns.module';
import { AnalyticsModule } from './components/crm/analytics/analytics.module';
import { SettingsModule } from './components/crm/settings/settings.module';
import { LandingModule } from './components/landing/landing.module';


@NgModule({
  declarations: [
    AppComponent,
    MapComponent,
    ChartsComponent,
  ],
  imports: [
    CrmModule,
    GuideModule,
    LeadsModule,
    FormsCrmModule,
    CampaignsModule,
    AnalyticsModule,
    SettingsModule,
    LandingModule,
    HttpClientModule,
    BrowserModule,
    BrowserAnimationsModule,
    MatButtonModule,
    AppRoutingModule
   ],
  providers: [
    { provide: LocationStrategy, useClass: PathLocationStrategy }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
