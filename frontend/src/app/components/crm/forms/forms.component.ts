import { Component, OnInit } from '@angular/core';
import { Form, FormsResponse } from '../../../models/server_responses/forms-response.model';
import { MatDialog } from '@angular/material/dialog';
import { LinkFormDialogComponent } from './link-form-dialog/link-form-dialog.component';
import { LinkFormResponse } from 'src/app/models/server_responses/link-form-response.model';
import { FormService } from '../../../services/form.service';
import { WebHookResponse } from 'src/app/models/server_responses/webhook-response.model';

@Component({
  selector: 'app-forms',
  templateUrl: './forms.component.html',
  styleUrls: ['./forms.component.css']
})
export class FormsComponent implements OnInit {
  keyOrdering: string[] = ['formId', 'formName', 'googleKey', 'verified', 'date'];
  forms: Form[] = [];
  webhook: string;

  constructor(public dialog: MatDialog, private formService: FormService) {
    this.formService.getForms().subscribe((result: FormsResponse) => {
      this.forms = result.forms as Form[];
      this.webhook = result.webhookUrl;
      console.log(result.forms);
    });
  }

  ngOnInit(): void { }


  openDialog() {
    const dialogRef = this.dialog.open(LinkFormDialogComponent, {
      data: {
        form_name: '',
        form_id: ''
      }
    });
    dialogRef.afterClosed().subscribe((result: LinkFormResponse) => {
      this.formService.linkForm(result).subscribe((result: WebHookResponse) => {
        console.log(result);
      });
    });
  }
}
