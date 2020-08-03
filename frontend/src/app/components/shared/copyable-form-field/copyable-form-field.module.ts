import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { ClipboardModule } from '@angular/cdk/clipboard';
import { MatIconModule } from '@angular/material/icon';
import { CopyableFormFieldComponent } from './copyable-form-field.component';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatInputModule } from '@angular/material/input';


@NgModule({
  declarations: [
    CopyableFormFieldComponent
  ],
  exports: [
    CopyableFormFieldComponent
  ],
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatButtonModule,
    ClipboardModule,
    MatIconModule,
    MatTooltipModule,
    MatButtonModule,
    MatInputModule
  ]
})
export class CopyableFormFieldModule { }
