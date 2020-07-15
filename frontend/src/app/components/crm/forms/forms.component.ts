import { Component, OnInit, ViewChild } from '@angular/core';
import { Form } from '../../../models/server_responses/forms-response.model';
import { MatDialog } from '@angular/material/dialog';
import { LinkFormDialogComponent } from './link-form-dialog/link-form-dialog.component';
import { FormService } from '../../../services/form.service';
import { CrmTableComponent } from '../../shared/crm-table/crm-table.component';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { WebhookService } from 'src/app/services/webhook.service';
import { WebHookResponse } from 'src/app/models/server_responses/webhook-response.model';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-forms',
  templateUrl: './forms.component.html',
  styleUrls: ['./forms.component.css']
})
export class FormsComponent implements OnInit {
  @ViewChild('formsCrmTable') formsTable: CrmTableComponent<Form>;
  forms: Observable<Form[]> = this.formService.getForms().pipe(
    map(res => res.forms)
  );
  keyOrdering: string[] = ['formId', 'formName', 'date'];
  webhookUrl = '';
  googlekey = '';

  constructor(
    public dialog: MatDialog, private formService: FormService,
    private webhookService: WebhookService, private titleService: Title) {
      this.titleService.setTitle('Forms');
  }

  ngOnInit(): void {
    this.webhookService.getWebhook().subscribe((res: WebHookResponse) => {
      this.webhookUrl = res.webhookUrl;
      this.googlekey = res.googleKey;
    });
  }

  openLinkFormDialog() {
    const dialogRef = this.dialog.open(LinkFormDialogComponent, {
      data: {
        form_name: '',
        form_id: ''
      }
    });
    dialogRef.afterClosed().subscribe((_any: any) => {
      // refresh because we added a form row
      this.formsTable.refreshDataSource();
    });
  }

  deleteForms(): void {
    this.formService.unlinkForms(this.formsTable.selection.selected)
    .subscribe((_any: any) => {
      if (this.formsTable !== undefined) {
        // refresh because we deleted some form rows
        this.formsTable.refreshDataSource();
      }
    });
  }
}
