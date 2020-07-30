import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CrmTableComponent } from './crm-table.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatTableModule } from '@angular/material/table';
import { FormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { AppRoutingModule } from 'src/app/app-routing.module';

@NgModule({
  declarations: [
    CrmTableComponent
  ],
  exports: [
    CrmTableComponent
  ],
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatIconModule,
    MatCheckboxModule,
    MatTableModule,
    FormsModule,
    MatInputModule,
    MatButtonModule
  ]
})
export class CrmTableModule { }
