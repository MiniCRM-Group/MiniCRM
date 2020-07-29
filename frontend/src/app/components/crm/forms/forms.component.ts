import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { Form, FormsResponse } from '../../../models/server_responses/forms-response.model';
import { FormService } from '../../../services/form.service';
import { CrmTableComponent } from '../../shared/crm-table/crm-table.component';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { WebhookService } from 'src/app/services/webhook.service';
import { WebHookResponse } from 'src/app/models/server_responses/webhook-response.model';
import { Title } from '@angular/platform-browser';
import { KeyDisplayedNameMap } from 'src/app/models/component_states/table-data.model';

@Component({
  selector: 'app-forms',
  templateUrl: './forms.component.html',
  styleUrls: ['./forms.component.css']
})
export class FormsComponent implements OnInit {
  @ViewChild('formsCrmTable') formsTable: CrmTableComponent<Form>;
  keyOrdering: string[] = ['formId', 'formName', 'date'];
  maps: KeyDisplayedNameMap[] = [
    {
      key: 'formId',
      displayedName: $localize`Form Id`
    },
    {
      key: 'formName',
      displayedName: $localize`Form Name`
    },
    {
      key: 'date',
      displayedName: $localize`Date`
    }
  ];

  constructor(
    private formService: FormService,
    private titleService: Title) {
      this.titleService.setTitle($localize`Forms`);
  }

  ngOnInit() {

  }
  
  /**
   * Renames the given form based on the id to the current name in the form object.
   * Called by the rename event from app-crm-table
   * @param form the form to be renamed
   */
  renameForm(form: Form) {
    this.formService.renameForm(form).subscribe((res: FormsResponse) => {
      this.formsTable.data = res.forms;
      this.formsTable.refreshDataSource();
    });
  }
}
