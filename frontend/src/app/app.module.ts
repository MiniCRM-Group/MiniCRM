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
import { MatMenuModule } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';

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
import { LandingComponent } from './components/landing/landing.component';
import { CrmTableComponent } from './components/shared/crm-table/crm-table.component';
import { LinkFormDialogComponent } from './components/crm/forms/link-form-dialog/link-form-dialog.component';
import { CopyableFormFieldComponent } from './components/shared/copyable-form-field/copyable-form-field.component';

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
    FormsComponent,
    CrmTableComponent,
    LinkFormDialogComponent,
    CopyableFormFieldComponent
  ],
  imports: [
    HttpClientModule,
    BrowserModule,
    FormsModule,
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
    MatMenuModule,
    MatTooltipModule
   ],
  providers: [
    { provide: LocationStrategy, useClass: PathLocationStrategy }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
