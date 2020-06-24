import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
    {
        path:'guide',
        loadChildren:'./customers/guide.module#GuidesModule'
    },
    {
        path:'campaigns',
        loadChildren:'./orders/campaigns.module#CampaignModule'
    },
    {
        path:'leads',
        loadChildren:'./leads/leads.module#LeadsModule'
    },
    {
        path:'analytics',
        loadChildren:'./leads/analytics.module#AnalyticsModule'
    },
    {
        path: '',
        redirectTo: '',
        pathMatch: 'full'
    }
];

@NgModule({
    imports: [ RouterModule.forRoot(routes)],
    exports: [ RouterModule ]
})
export class AppRoutingModule {}
