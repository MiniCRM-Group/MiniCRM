import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { LinkFormResponse } from '../../../../models/server_responses/link-form-response.model';

@Component({
  selector: 'app-link-form-dialog',
  templateUrl: './link-form-dialog.component.html',
  styleUrls: ['./link-form-dialog.component.css']
})
export class LinkFormDialogComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<LinkFormDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: LinkFormResponse) { }

  ngOnInit(): void {
  }

}
