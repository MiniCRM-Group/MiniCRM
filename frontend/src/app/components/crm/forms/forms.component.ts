import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { Form, FormsResponse } from '../../../models/server_responses/forms-response.model';
import { MatDialog } from '@angular/material/dialog';
import { LinkFormDialogComponent } from './link-form-dialog/link-form-dialog.component';
import { FormService } from '../../../services/form.service';
import { CrmTableComponent } from '../../shared/crm-table/crm-table.component';
import { map, first } from 'rxjs/operators';
import { Observable } from 'rxjs';

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
  keyOrdering: string[] = ['formId', 'formName', 'googleKey', 'verified', 'date'];
  webhook: string = "";

  constructor(public dialog: MatDialog, private formService: FormService) {
  }

  ngOnInit(): void {
  }

  openLinkFormDialog() {
    const dialogRef = this.dialog.open(LinkFormDialogComponent, {
      data: {
        form_name: '',
        form_id: ''
      }
    });
  }

  deleteForms(): void {
    console.log(this.formsTable.selection.selected);
  }
}
