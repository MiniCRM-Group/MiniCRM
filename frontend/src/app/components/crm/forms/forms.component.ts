import { Component, OnInit, ViewChild } from '@angular/core';
import { Form } from '../../../models/server_responses/forms-response.model';
import { MatDialog } from '@angular/material/dialog';
import { FormService } from '../../../services/form.service';
import { CrmTableComponent } from '../../shared/crm-table/crm-table.component';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
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

  constructor(
    public dialog: MatDialog, private formService: FormService, 
    private titleService: Title) {
      this.titleService.setTitle('Forms');
  }

  ngOnInit(): void {
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
