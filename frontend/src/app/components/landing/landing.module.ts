import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LandingComponent } from './landing.component';
import { MatIconModule } from '@angular/material/icon';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';


@NgModule({
  exports: [LandingComponent],
  declarations: [LandingComponent],
  imports: [
    CommonModule,
    MatIconModule,
    FlexLayoutModule,
    MatToolbarModule,
    MatButtonModule
  ]
})
export class LandingModule { }
