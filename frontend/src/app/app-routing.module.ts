import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LandingComponent } from './components/landing/landing.component';
import { CrmComponent } from './components/crm/crm.component';
import { LeadsComponent } from './components/crm/leads/leads.component';
import { FormsComponent } from './components/crm/forms/forms.component';
import { CampaignsComponent } from './components/crm/campaigns/campaigns.component';
import { GuideComponent } from './components/crm/guide/guide.component';
import { AnalyticsComponent } from './components/crm/analytics/analytics.component';
import { SettingsComponent } from './components/crm/settings/settings.component';

const routes: Routes = [
    { path: '', component: LandingComponent },
    {
        path: 'crm',
        component: CrmComponent,
        children: [
            { path: '', component: GuideComponent },
            { path: 'leads', component: LeadsComponent },
            { path: 'forms', component: FormsComponent },
            { path: 'campaigns', component: CampaignsComponent },
            { path: 'analytics', component: AnalyticsComponent },
            { path: 'guide', component: GuideComponent },
            { path: 'settings', component: SettingsComponent }
        ]
    }
];

@NgModule({
    imports: [
        RouterModule.forRoot(routes)
    ],
    exports: [ RouterModule ]
})
export class AppRoutingModule {}
