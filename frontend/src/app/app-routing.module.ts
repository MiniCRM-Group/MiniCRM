import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LeadsComponent }   from './leads/leads.component';
import { CampaignsComponent }   from './campaigns/campaigns.component';
import { GuideComponent }   from './guide/guide.component';
import { AnalyticsComponent }   from './analytics/analytics.component';
const  introModule = () => import('./intro/intro.module').then(x => x.IntroModule);
const routes: Routes = [
    {
        path: '',
        redirectTo: '',
        pathMatch: 'full'
    },
    { path: 'leads', component: LeadsComponent },
    { path: 'campaigns', component: CampaignsComponent },
    { path: 'analytics', component: AnalyticsComponent },
    { path: 'guide', component: GuideComponent },
    { path: 'intro', loadChildren: introModule },
];

@NgModule({
    imports: [ RouterModule.forRoot(routes)],
    exports: [ RouterModule ]
})
export class AppRoutingModule {}
