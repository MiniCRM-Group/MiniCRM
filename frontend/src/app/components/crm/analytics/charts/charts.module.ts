import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChartsComponent } from './charts.component';



@NgModule({
  declarations: [
    ChartsComponent
  ],
  exports: [
    ChartsComponent
  ],
  imports: [
    CommonModule
  ]
})
export class ChartsModule { }
