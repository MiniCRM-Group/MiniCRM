import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { LinkFormRequest } from '../../../../models/server_requests/link-form-request.model';
import { FormService } from '../../../../services/form.service';
import { WebHookResponse } from 'src/app/models/server_responses/webhook-response.model';

@Component({
  selector: 'app-link-form-dialog',
  templateUrl: './link-form-dialog.component.html',
  styleUrls: ['./link-form-dialog.component.css']
})
export class LinkFormDialogComponent implements OnInit {
  errorMessage: string;

  constructor(
    public dialogRef: MatDialogRef<LinkFormDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: LinkFormRequest,
    private formService: FormService) { }

  ngOnInit(): void {
  }

  onLink() {
    this.formService.linkForm(this.data).subscribe(
      (result: WebHookResponse) => {
        this.dialogRef.close();
      },
      (error: string) => {
        this.errorMessage = error;
      }
    )
  }
}
