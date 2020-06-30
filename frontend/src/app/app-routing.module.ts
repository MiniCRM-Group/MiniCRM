import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LandingComponent } from './landing/landing.component';
import { CrmComponent } from './crm/crm.component';
import { LeadsComponent }   from './crm/leads/leads.component';
import { FormsComponent } from './crm/forms/forms.component';
import { CampaignsComponent }   from './crm/campaigns/campaigns.component';
import { GuideComponent }   from './crm/guide/guide.component';
import { AnalyticsComponent }   from './crm/analytics/analytics.component';

const routes: Routes = [
    { path: '', component: LandingComponent },
    { 
        path: 'crm', 
        component: CrmComponent,
        children: [
            { path: 'leads', component: LeadsComponent },
            { path: 'forms', component: FormsComponent },
            { path: 'campaigns', component: CampaignsComponent },
            { path: 'analytics', component: AnalyticsComponent },
            { path: 'guide', component: GuideComponent }
        ] 
    }
];

@NgModule({
    imports: [ RouterModule.forRoot(routes)],
    exports: [ RouterModule ]
})
export class AppRoutingModule {}
