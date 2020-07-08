import { Component, OnInit, ViewChild, AfterViewChecked } from '@angular/core';
import { Form, FormsResponse } from '../../../models/server_responses/forms-response.model';
import { MatDialog } from '@angular/material/dialog';
import { LinkFormDialogComponent } from './link-form-dialog/link-form-dialog.component';
import { LinkFormResponse } from 'src/app/models/server_responses/link-form-response.model';
import { FormService } from '../../../services/form.service';
import { WebHookResponse } from 'src/app/models/server_responses/webhook-response.model';
import { MatTableDataSource } from '@angular/material/table';
import { TableData } from 'src/app/models/component_states/table-data.model';
import { CrmTableComponent } from '../../shared/crm-table/crm-table.component';
import { SelectionModel } from '@angular/cdk/collections';

@Component({
  selector: 'app-forms',
  templateUrl: './forms.component.html',
  styleUrls: ['./forms.component.css']
})
export class FormsComponent implements OnInit, AfterViewChecked {
  @ViewChild('formsCrmTable') formsTable: CrmTableComponent<Form>;
  formsTableData: TableData<Form> = {
    // use this to smoothly render the table
    dataSource: new MatTableDataSource<Form>([
      {
        formId: 1,
        formName: 'form 1',
        googleKey: 'dfggsgf',
        verified: true,
        date: 'Jan 2015'
      }
    ]),
    // this is the order that the columns will be rendered
    keyOrdering: ['formId', 'formName', 'googleKey', 'verified', 'date'],
    // if selection is defined, then a column of checkboxes is rendered
    selection: new SelectionModel(/*multiselect=*/true, [])
  };

  webhook: string = 'minicrm.com';

  constructor(public dialog: MatDialog, private formService: FormService) {
  }

  ngOnInit(): void {
  }
  
  ngAfterViewChecked(): void {
    console.log(this.formsTable);
    this.formsTable.tableData = this.formsTableData;
  }

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

  deleteForms(): void {
    console.log(this.formsTableData.selection.selected);
  }

  updateAll() {
    // make a call to server to request all forms
    this.formService.getForms().subscribe((result: FormsResponse) => {
      // You set the datasource's data field to render the table rows.
      this.formsTableData.dataSource.data = result.forms;
      this.webhook = result.webhookUrl;
    });
  }
}
