import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsComponent } from './forms.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { CrmTableModule } from '../../shared/crm-table/crm-table.module';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  declarations: [
    FormsComponent
  ],
  exports: [
    FormsComponent
  ],
  imports: [
    CommonModule,
    FlexLayoutModule,
    CrmTableModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule
  ]
})
export class FormsCrmModule { }
