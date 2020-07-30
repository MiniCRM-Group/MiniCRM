import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GuideComponent } from './guide.component';
import { FlexLayoutModule } from '@angular/flex-layout';

@NgModule({
  declarations: [
    GuideComponent
  ],
  exports: [
    GuideComponent
  ],
  imports: [
    CommonModule,
    FlexLayoutModule
  ]
})
export class GuideModule { }
