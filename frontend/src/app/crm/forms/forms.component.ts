import { Component, OnInit } from '@angular/core';
import { Form } from '../../models/form.model';

const FORM_DATA: Form[] = [
  {
    formId: 1,
    formName: 'Form 1',
    date: '2016-03-14',
    googleKey: 'googleKey',
    verified: true
  },
  {
    formId: 1,
    formName: 'Form 1',
    date: '2016-03-14',
    googleKey: 'googleKey',
    verified: false
  }
];

@Component({
  selector: 'app-forms',
  templateUrl: './forms.component.html',
  styleUrls: ['./forms.component.css']
})
export class FormsComponent implements OnInit {
  displayedColumns: string[] = ['formId', 'formName', 'googleKey', 'verified', 'date'];
  dataSource: Form[] = FORM_DATA;

  constructor() { }

  ngOnInit(): void {
  }

}
