import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { Form, FormsResponse } from '../../../models/server_responses/forms-response.model';
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
export class FormsComponent implements OnInit, AfterViewInit {
  @ViewChild('formsCrmTable') formsTable: CrmTableComponent<Form>;
  keyOrdering: string[] = ['formId', 'formName', 'date'];

  constructor(
    public dialog: MatDialog, private formService: FormService,
    private titleService: Title) {
      this.titleService.setTitle($localize `Forms`);
  }

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
    this.formService.getForms().subscribe((res: FormsResponse) => {
      this.formsTable.data = res.forms;
      this.formsTable.refreshDataSource();
    });
  }

  /**
   * Renames the given form based on the id to the current name in the form object.
   * Called by the rename event from app-crm-table
   * @param form the form to be renamed
   */
  renameForm(form: Form) {
    this.formService.renameForm(form).subscribe();
  }
}
