import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LinkFormDialogComponent } from './link-form-dialog.component';

describe('LinkFormDialogComponent', () => {
  let component: LinkFormDialogComponent;
  let fixture: ComponentFixture<LinkFormDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LinkFormDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LinkFormDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
