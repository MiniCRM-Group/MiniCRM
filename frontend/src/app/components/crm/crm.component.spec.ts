import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { CrmComponent } from './crm.component';
import { LoginService } from 'src/app/services/login.service';
import { of } from 'rxjs';

describe('CrmComponent', () => {
  let component: CrmComponent;
  const loginService: Partial<LoginService> = {
    getLoginResponse: () => of({ loggedIn: false, url: '/' })
  };
  let fixture: ComponentFixture<CrmComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CrmComponent ],
      providers: [
        { provide: LoginService, useValue: loginService }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CrmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
