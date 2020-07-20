import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CrmTableComponent } from './crm-table.component';

describe('CrmTableComponent', () => {
  let component: CrmTableComponent<{}>;
  let fixture: ComponentFixture<CrmTableComponent<{}>>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CrmTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CrmTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
