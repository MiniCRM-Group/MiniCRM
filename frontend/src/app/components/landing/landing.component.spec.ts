import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LandingComponent } from './landing.component';
import { LoginService } from 'src/app/services/login.service';
import { of } from 'rxjs';
import { LoginResponse } from 'src/app/models/server_responses/login-response.model';
import { MatIconModule } from '@angular/material/icon';

describe('LandingComponent', () => {
  let component: LandingComponent;
  const loginService: Partial<LoginService> = {
    getLoginResponse: () => of<LoginResponse>({ loggedIn: false, url: '/' })
  };
  let fixture: ComponentFixture<LandingComponent>;

  beforeEach(async(() => {
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
