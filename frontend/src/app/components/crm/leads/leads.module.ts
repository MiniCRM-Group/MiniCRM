import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LeadsComponent } from './leads.component';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatTableExporterModule } from 'mat-table-exporter';
import { MatOptionModule } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';
import { LeadDetailsComponent } from './lead-details/lead-details.component';
import { MatListModule } from '@angular/material/list';
import { MatDialogModule } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { HumanizeEnumPipe } from 'src/app/pipes/humanize-enum.pipe';
import { FormsModule } from '@angular/forms';
import { MatSortModule } from '@angular/material/sort';
import { FlexLayoutModule } from '@angular/flex-layout';


@NgModule({
  declarations: [
    LeadsComponent,
    LeadDetailsComponent,
    HumanizeEnumPipe,
  ],
  exports: [
    LeadsComponent,
  ],
  imports: [
    FlexLayoutModule,
    FormsModule,
    CommonModule,
    MatIconModule,
    MatSortModule,
    MatFormFieldModule,
    MatTableModule,
    MatTableExporterModule,
    MatCheckboxModule,
    MatCardModule,
    MatProgressSpinnerModule,
    MatPaginatorModule,
    MatOptionModule,
    MatSelectModule,
    MatListModule,
    MatDialogModule,
    MatInputModule,
    MatButtonModule
  ]
})
export class LeadsModule { }
