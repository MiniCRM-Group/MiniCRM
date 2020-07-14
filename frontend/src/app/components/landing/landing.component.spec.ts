import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LandingComponent } from './landing.component';
import { LoginService } from 'src/app/services/login.service';
import { of } from 'rxjs';
import { LoginResponse } from 'src/app/models/server_responses/login-response.model';
import { MatIconModule } from '@angular/material/icon';

describe('LandingComponent', () => {
  let component: LandingComponent;
  let fixture: ComponentFixture<LandingComponent>;

  beforeEach(async(() => {
    let loginResponse: LoginResponse = {
      url: '/crm/guide',
      loggedIn: false
    };
    const loginService = jasmine.createSpyObj('LoginService', ['getLoginResponse']);
    const getLoginResponseSpy = loginService.getLoginResponse.and.returnValue( of(loginResponse) )
    TestBed.configureTestingModule({
      imports: [ MatIconModule ],
      declarations: [ LandingComponent ],
      providers: [
        { provide: LoginService, useValue: loginService }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LandingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
