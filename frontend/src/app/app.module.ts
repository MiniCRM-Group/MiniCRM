import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

// Flex + Grid Layout
import { FlexLayoutModule } from '@angular/flex-layout';

// Material imports
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';


// Routing
import { AppRoutingModule } from './app-routing.module';
import { LocationStrategy, PathLocationStrategy } from '@angular/common';

// Component imports
import { AppComponent } from './app.component';
import { CrmComponent } from './crm/crm.component';
import { LeadsComponent } from './crm/leads/leads.component';
import { FormsComponent } from './crm/forms/forms.component';
import { CampaignsComponent } from './crm/campaigns/campaigns.component';
import { GuideComponent } from './crm/guide/guide.component';
import { AnalyticsComponent } from './crm/analytics/analytics.component';
import { LandingComponent } from './landing/landing.component';

// Service imports
import { LeadService } from './services/lead.service';

@NgModule({
  declarations: [
    AppComponent,
    LeadsComponent,
    FormsComponent,
    CampaignsComponent,
    GuideComponent,
    AnalyticsComponent,
    CrmComponent,
    LandingComponent,
    FormsComponent
  ],
  imports: [
    HttpClientModule,
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FlexLayoutModule,

    // Material
    BrowserAnimationsModule,
    MatButtonModule,
    MatIconModule,
    MatSidenavModule,
    MatToolbarModule,
    MatTableModule,
    MatPaginatorModule,
    MatFormFieldModule,
    MatInputModule
  ],
  providers: [
    LeadService,
    { provide: LocationStrategy, useClass: PathLocationStrategy }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
