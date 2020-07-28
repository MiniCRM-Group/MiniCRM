import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-lead-details',
  templateUrl: 'lead-details.component.html',
  styleUrls: ['./lead-details.component.css']
})
export class LeadDetailsComponent {
  private initialNotes: string;
  updated = false;
  constructor(
    public dialogRef: MatDialogRef<LeadDetailsComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) {
      this.initialNotes = data.lead.notes;
     }

  onNoClick(): void {
    this.dialogRef.close();
  }

  notesUpdated() {
    this.updated = (this.initialNotes !== this.data.lead.notes);
  }
}
