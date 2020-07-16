import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-lead-details',
  templateUrl: 'lead-details.component.html',
})
export class LeadDetailsComponent {

  constructor(
    public dialogRef: MatDialogRef<LeadDetailsComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) { }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
