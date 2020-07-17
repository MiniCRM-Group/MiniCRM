import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CopyableFormFieldComponent } from './copyable-form-field.component';
import { MatTooltipModule } from '@angular/material/tooltip';

describe('CopyableFormFieldComponent', () => {
  let component: CopyableFormFieldComponent;
  let fixture: ComponentFixture<CopyableFormFieldComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CopyableFormFieldComponent ],
      imports: [
        MatTooltipModule
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CopyableFormFieldComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
