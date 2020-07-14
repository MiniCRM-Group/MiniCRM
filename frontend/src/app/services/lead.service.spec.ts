import { TestBed } from '@angular/core/testing';

import { LeadService } from './lead.service';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';

describe('LeadService', () => {
  let service: LeadService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ HttpClientTestingModule ],
      providers: [ LeadService ]
    });
    service = TestBed.inject(LeadService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
