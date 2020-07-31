import { TestBed } from '@angular/core/testing';

import { CampaignService } from './campaign.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { FormService } from './form.service';
import { Campaign, CampaignsResponse } from '../models/server_responses/campaign.model';

describe('CampaignService', () => {
  const dummyCampaignsResponse: CampaignsResponse = {
    campaigns: [
      { campaignId: 1, campaignName: 'Campaign 1', date: Date().toString() },
      { campaignId: 2, campaignName: 'Campaign 2', date: Date().toString() }
    ] as Campaign[]
  };

  let service: CampaignService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ HttpClientTestingModule ],
      providers: [ CampaignService ]
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

  describe('getCampaigns', () => {
    it('should return an Observable<FormResponse> of all campaigns', () => {
      service.getCampaigns().subscribe((res: CampaignsResponse) => {
        expect(res.campaigns).toEqual(dummyCampaignsResponse.campaigns);
      });
      const req = httpMock.expectOne(service.endpoint);
      expect(req.request.method).toBe('GET');
      req.flush(dummyCampaignsResponse);
    });
  });

  describe('renameCampaign', () => {
    it('should do a PUT request with the campaign id and name', () => {
      const campaign: Campaign = {date: Date().toString(), campaignId: 1, campaignName: 'newName' } as Campaign;

      service.renameCampaign(campaign).subscribe(/* Don't care about the response */);

      // expect a PUT request with all the correct parameters in the body
      const req = httpMock.expectOne(service.endpoint);
      const request = req.request;
      expect(request.method).toBe('PUT');
      expect(request.body.campaignId).toBe('1');
      expect(request.body.campaignName).toBe('newName');
    });
  });

  describe('getCampaignNameMap', () => {
    it('should return a map containing all campaign ids mapped to correct names', () => {
      const expectedMap: Map<number, string> = new Map();
      expectedMap.set(1, 'Campaign 1');
      expectedMap.set(2, 'Campaign 2');

      service.getCampaignNameMap().subscribe((map: Map<number, string>) => {
        expect(map).toEqual(expectedMap);
      });

      const req = httpMock.expectOne(service.endpoint);
      const request = req.request;
      expect(request.method).toBe('GET');
      req.flush(dummyCampaignsResponse);
    });

    it('should return an empty map with no campaigns', () => {
      const expectedMap: Map<number, string> = new Map();

      service.getCampaignNameMap().subscribe((map: Map<number, string>) => {
        expect(map).toEqual(expectedMap);
      });

      const req = httpMock.expectOne(service.endpoint);
      const request = req.request;
      expect(request.method).toBe('GET');
      req.flush({campaigns: [] as Campaign[]});
    });
  });
});
