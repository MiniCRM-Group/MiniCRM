import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { Form, FormsResponse } from '../../../models/server_responses/forms-response.model';
import { MatDialog } from '@angular/material/dialog';
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
export class FormsComponent implements OnInit, AfterViewInit {
  @ViewChild('formsCrmTable') formsTable: CrmTableComponent<Form>;
  keyOrdering: string[] = ['formId', 'formName', 'date'];
  webhookUrl = '';
  googlekey = '';

  constructor(
    public dialog: MatDialog, private formService: FormService,
    private webhookService: WebhookService, private titleService: Title) {
      this.titleService.setTitle($localize `Forms`);
  }

  ngOnInit(): void {
    this.webhookService.getWebhook().subscribe((res: WebHookResponse) => {
      this.webhookUrl = res.webhookUrl;
      this.googlekey = res.googleKey;
    });
  }

  ngAfterViewInit(): void {
    this.formService.getForms().subscribe((res: FormsResponse) => {
      this.formsTable.data = res.forms;
      this.formsTable.refreshDataSource();
    });
  }

  deleteForms(): void {
    this.formService.unlinkForms(this.formsTable.selection.selected)
    .subscribe((_: any) => {
      if (this.formsTable !== undefined) {
        // refresh because we deleted some form rows
        this.formsTable.refreshDataSource();
      }
    });
  }
}
