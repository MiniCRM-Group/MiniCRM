import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GuideRoutingModule } from './guide-routing.module';
import { GuideInfoComponent } from './guide-info/guide-info.component';


@NgModule({
  declarations: [GuideInfoComponent],
  imports: [
    CommonModule,
    GuideRoutingModule
  ]
})
export class GuideModule { }
