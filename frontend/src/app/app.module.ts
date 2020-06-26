import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { HttpClientModule } from '@angular/common/http';
import { LeadService } from './shared/lead.service';

// Material section
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';

// Routing
import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
import { LeadsComponent } from './leads/leads.component';
import { CampaignsComponent } from './campaigns/campaigns.component';
import { GuideComponent } from './guide/guide.component';
import { AnalyticsComponent } from './analytics/analytics.component';
import { IntroComponent } from './intro/intro.component';
@NgModule({
  declarations: [
    AppComponent,
    LeadsComponent,
    CampaignsComponent,
    GuideComponent,
    AnalyticsComponent
  ],
  imports: [
    HttpClientModule,
    BrowserModule,
    AppRoutingModule,

    // Material
    BrowserAnimationsModule,
    MatButtonModule,
    MatIconModule,
    MatSidenavModule,
    MatToolbarModule
  ],
  providers: [LeadService],
  bootstrap: [AppComponent]
})
export class AppModule { }
