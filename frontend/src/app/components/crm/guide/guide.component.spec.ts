import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GuideComponent } from './guide.component';
import { GuideModule } from './guide.module';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('GuideComponent', () => {
  let component: GuideComponent;
  let fixture: ComponentFixture<GuideComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ GuideModule, NoopAnimationsModule],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GuideComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
