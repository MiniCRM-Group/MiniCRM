import { TestBed } from '@angular/core/testing';

import { CampaignService } from './campaign.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { FormService } from './form.service';

describe('CampaignService', () => {
  let service: CampaignService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ HttpClientTestingModule ],
      providers: [ FormService ]
    });
    service = TestBed.inject(CampaignService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
