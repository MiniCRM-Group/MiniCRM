import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AnalyticsPanelComponent } from './analytics-panel.component';

describe('AnalyticsPanelComponent', () => {
  let component: AnalyticsPanelComponent;
  let fixture: ComponentFixture<AnalyticsPanelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AnalyticsPanelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AnalyticsPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
