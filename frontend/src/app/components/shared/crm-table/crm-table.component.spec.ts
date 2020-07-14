import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CrmTableComponent } from './crm-table.component';
import { Form } from 'src/app/models/server_responses/forms-response.model';

describe('CrmTableComponent', () => {
  let component: CrmTableComponent<Form>;
  let fixture: ComponentFixture<CrmTableComponent<Form>>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CrmTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent<CrmTableComponent<Form>>(CrmTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
