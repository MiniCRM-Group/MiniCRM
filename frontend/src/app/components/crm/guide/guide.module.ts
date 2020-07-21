import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GuideComponent } from './guide.component';

@NgModule({
  declarations: [GuideComponent],
  exports: [GuideComponent],
  imports: [
    CommonModule
  ]
})
export class GuideModule { }
