import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CrmTableComponent } from './crm-table.component';
import { Form } from 'src/app/models/server_responses/forms-response.model';
import { Component, ViewChild } from '@angular/core';
import { Observable, of } from 'rxjs';

describe('CrmTableComponent', () => {
  let component: TestCrmTableComponent;
  let fixture: ComponentFixture<TestCrmTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CrmTableComponent, TestCrmTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TestCrmTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

@Component({
  selector: 'app-test-crm-table',
  template: `
    <app-crm-table
    #testCrmTable
      [data]="data"
      [keyOrdering]="keyOrdering"
      [selectionEnabled]="selectionEnabled">
    </app-crm-table>
  `
})
class TestCrmTableComponent {
  @ViewChild('testCrmTable') testTable: CrmTableComponent<TestTabularData>;
  private data: Observable<TestTabularData[]> = of([
    {
      name: 'Roddy',
      age: 20,
      hasPet: true
    }
  ]);
  private keyOrdering: string[] = ['name', 'age', 'hasPet'];
  private selectionEnabled = false;
  public setData(newData: Observable<TestTabularData[]>) {
    this.data = newData;
  }
}

interface TestTabularData {
  name: string;
  age: number;
  hasPet: boolean;
}
