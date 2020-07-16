import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { CrmComponent } from './crm.component';
import { LoginService } from 'src/app/services/login.service';
import { of, Observable } from 'rxjs';
import { LoginResponse } from 'src/app/models/server_responses/login-response.model';

describe('CrmComponent', () => {
  let component: CrmComponent;
  let loginService: LoginService;
  let fixture: ComponentFixture<CrmComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CrmComponent ],
      providers: [
        { 
          provide: LoginService, 
          useValue: {
            getLoginResponse: (): Observable<LoginResponse> => of({
              loggedIn: true,
              url: ''
            })
          }
        }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CrmComponent);
    component = fixture.componentInstance;
    loginService = TestBed.get(LoginService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
