import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CrmComponent } from './crm.component';
import { LoginResponse } from 'src/app/models/server_responses/login-response.model';
import { of } from 'rxjs';
import { LoginService } from 'src/app/services/login.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('CrmComponent', () => {
  let component: CrmComponent;
  let fixture: ComponentFixture<CrmComponent>;

  beforeEach(async(() => {
    let loginResponse: LoginResponse = {
      url: '/',
      loggedIn: true
    };
    const loginService = jasmine.createSpyObj('LoginService', ['getLoginResponse']);
    const getLoginResponseSpy = loginService.getLoginResponse.and.returnValue( of(loginResponse) );
    TestBed.configureTestingModule({
      declarations: [ CrmComponent, HttpClientTestingModule ],
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
