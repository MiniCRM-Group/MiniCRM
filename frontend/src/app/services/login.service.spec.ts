import { TestBed } from '@angular/core/testing';

import { LoginService } from './login.service';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { FormService } from './form.service';

describe('LoginService', () => {
  let service: LoginService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ HttpClientTestingModule ],
      providers: [ LoginService ]
    });    
    service = TestBed.inject(LoginService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
