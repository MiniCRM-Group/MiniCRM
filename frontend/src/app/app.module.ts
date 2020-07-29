import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

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
import { ClipboardModule } from '@angular/cdk/clipboard';
import { MatSortModule } from '@angular/material/sort';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDialogModule } from '@angular/material/dialog';
import { MatListModule } from '@angular/material/list';
import { MatSelectModule } from '@angular/material/select';
import { MatMenuModule } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatTabsModule } from '@angular/material/tabs';
import { MatTableExporterModule } from 'mat-table-exporter';
import { MatStepperModule } from '@angular/material/stepper';

import {} from 'googlemaps';


import { GoogleMapsModule } from '@angular/google-maps';

// Routing
import { AppRoutingModule } from './app-routing.module';
import { LocationStrategy, PathLocationStrategy } from '@angular/common';

// Component imports
import { AppComponent } from './app.component';
import { CrmComponent } from './components/crm/crm.component';
import { LeadsComponent } from './components/crm/leads/leads.component';
import { FormsComponent } from './components/crm/forms/forms.component';
import { CampaignsComponent } from './components/crm/campaigns/campaigns.component';
import { GuideComponent } from './components/crm/guide/guide.component';
import { AnalyticsComponent } from './components/crm/analytics/analytics.component';
import { MapComponent } from './components/crm/analytics/map/map.component';
import { ChartsComponent } from './components/crm/analytics/charts/charts.component';
import { LandingComponent } from './components/landing/landing.component';
import { CrmTableComponent } from './components/shared/crm-table/crm-table.component';
import { CopyableFormFieldComponent } from './components/shared/copyable-form-field/copyable-form-field.component';
import { LeadDetailsComponent } from './components/crm/leads/lead-details/lead-details.component';
import { SettingsComponent } from './components/crm/settings/settings.component';

import { LeadsModule } from './components/crm/leads/leads.module';



@NgModule({
  declarations: [
    AppComponent,
    FormsComponent,
    CampaignsComponent,
    GuideComponent,
    AnalyticsComponent,
    MapComponent,
    ChartsComponent,
    CrmComponent,
    LandingComponent,
    FormsComponent,
    CrmTableComponent,
    CopyableFormFieldComponent,
    LeadDetailsComponent,
    SettingsComponent
  ],
  imports: [
    HttpClientModule,
    BrowserModule,
    FormsModule,
    LeadsModule,
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
    MatSortModule,
    MatInputModule,
    MatFormFieldModule,
    MatInputModule,
    MatCheckboxModule,
    ClipboardModule,
    MatDialogModule,
    MatProgressSpinnerModule,
    MatCardModule,
    MatCheckboxModule,
    MatDialogModule,
    MatListModule,
    MatSelectModule,
    MatMenuModule,
    MatTooltipModule,
    MatTabsModule,
    MatStepperModule,
    GoogleMapsModule,
    MatTableExporterModule
   ],
  providers: [
    { provide: LocationStrategy, useClass: PathLocationStrategy }
  ],
  entryComponents: [LeadDetailsComponent],
  bootstrap: [AppComponent]
})
export class AppModule { }
