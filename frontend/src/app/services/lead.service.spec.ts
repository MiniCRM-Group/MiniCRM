import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { LeadService } from './lead.service';
import { Lead, LeadsResponse } from '../models/server_responses/lead.model';
import { HttpClient, HttpResponse, HttpErrorResponse } from '@angular/common/http';

describe('LeadService', () => {
  let leadService: LeadService;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ HttpClientTestingModule ],
      providers: [ LeadService ]
    });
    httpClient = TestBed.inject(HttpClient);
    httpTestingController = TestBed.inject(HttpTestingController);
    leadService = TestBed.inject(LeadService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(leadService).toBeTruthy();
  });

  describe('Get all leads', () => {
    it('should return an Observable<Lead[]>', () => {
      const expectedLeads: LeadsResponse = {
        leads: [
      { date: new Date(), leadId: 'cKlwe8W4xN4CFcoFrwkdyHEByt', campaignId: 204256,
        gclId: 'uebai8W4xN4CFcoFrwkdyHEByw', apiVersion: '1.0', formId: 9574,
        googleKey: 'hWDxdHeMT1yq9DZgt8Io',
        columnData: {FULL_NAME: 'kali Ali', EMAIL: 'kali@gmail.com', PHONE_NUMBER: '+15334567890'},
        isTest: false, adgroupId : 0, creativeId: 0, status: 'OPEN', notes: ''},
      { date: new Date(), leadId: 'dKlwe8W4xN4CFcoFrwkdyHEByt', campaignId: 204256,
        gclId: 'uebai8W4xN4CFcoFrwkdyHEByw', apiVersion: '1.0', formId: 9574,
        googleKey: 'hWDxdHeMT1yq9DZgt8Io',
        columnData: {FULL_NAME: 'lali Ali', EMAIL: 'lali@gmail.com', PHONE_NUMBER: '+25334567890'},
        isTest: false, adgroupId : 0, creativeId: 0, status: 'OPEN', notes: ''}
        ] as Lead[]
      };

      leadService.getAllLeads().subscribe((res: Lead[]) => {
        expect(res).toEqual(expectedLeads.leads);
      });

      const req = httpMock.expectOne(leadService.url);
      expect(req.request.method).toBe('GET');
      req.flush(expectedLeads);
    });

    it('should be OK returning no leads', () => {
        const expectedLeads: LeadsResponse = {
        leads: [
            // empty Observable
        ] as Lead[]
      };
        leadService.getAllLeads().subscribe(
          leads => expect(expectedLeads.leads.length).toEqual(0, 'should have empty leads array'),
          fail
        );

        const req = httpTestingController.expectOne(leadService.url);
        req.flush([]); // Respond with no leads
    });

    it('should return expected leads (called multiple times)', () => {
      const expectedLeads: LeadsResponse = {
        leads: [
              { date: new Date(), leadId: 'cKlwe8W4xN4CFcoFrwkdyHEByt', campaignId: 204256,
                gclId: 'uebai8W4xN4CFcoFrwkdyHEByw', apiVersion: '1.0', formId: 9574,
                googleKey: 'hWDxdHeMT1yq9DZgt8Io',
                columnData: {FULL_NAME: 'kali Ali', EMAIL: 'kali@gmail.com', PHONE_NUMBER: '+15334567890'},
                isTest: false, adgroupId : 0, creativeId: 0, status: 'OPEN', notes: ''},
              { date: new Date(), leadId: 'dKlwe8W4xN4CFcoFrwkdyHEByt', campaignId: 204256,
                gclId: 'uebai8W4xN4CFcoFrwkdyHEByw', apiVersion: '1.0', formId: 9574,
                googleKey: 'hWDxdHeMT1yq9DZgt8Io',
                columnData: {FULL_NAME: 'lali Ali', EMAIL: 'lali@gmail.com', PHONE_NUMBER: '+25334567890'},
                isTest: false, adgroupId : 0, creativeId: 0, status: 'OPEN', notes: ''},
              { date: new Date(), leadId: 'fKlwe8W4xN4CFcoFrwkdyHEByt', campaignId: 204256,
                gclId: 'uebai8W4xN4CFcoFrwkdyHEByw', apiVersion: '1.0', formId: 9574,
                googleKey: 'hWDxdHeMT1yq9DZgt8Io',
                columnData: {FULL_NAME: 'Yali Ali', EMAIL: 'yali@gmail.com', PHONE_NUMBER: '+45334567890'},
                isTest: false, adgroupId : 0, creativeId: 0, status: 'OPEN', notes: ''}
        ] as Lead[]
    };
      leadService.getAllLeads().subscribe();
      leadService.getAllLeads().subscribe();
      leadService.getAllLeads().subscribe(
        leads => expect(leads).toEqual(expectedLeads.leads, 'should return expected heroes'),
        fail
      );

      const requests = httpTestingController.match(leadService.url);
      expect(requests.length).toEqual(3, 'calls to getHeroes()');

      // Respond to each request with different mock lead results
      requests[0].flush([]);
      requests[1].flush([{date: new Date(), leadId: 'dKlwe8W4xN4CFcoFrwkdyHEByt', campaignId: 204256,
                                        gclId: 'uebai8W4xN4CFcoFrwkdyHEByw', apiVersion: '1.0', formId: 9574,
                                        googleKey: 'hWDxdHeMT1yq9DZgt8Io',
                                        columnData: {FULL_NAME: 'lali Ali', EMAIL: 'lali@gmail.com', PHONE_NUMBER: '+25334567890'},
                                        isTest: false, adgroupId : 0, creativeId: 0, status: 'OPEN', notes: ''}]);
      requests[2].flush(expectedLeads);
    });
  });

  describe('Update Lead', () => {
    it('should do a PUT request with the lead parameters', () => {
      const lead: Lead = { date: new Date(), leadId: 'cKlwe8W4xN4CFcoFrwkdyHEByt', campaignId: 204256,
      gclId: 'uebai8W4xN4CFcoFrwkdyHEByw', apiVersion: '1.0', formId: 9574,
      googleKey: 'hWDxdHeMT1yq9DZgt8Io',
      columnData: {FULL_NAME: 'kali Ali', EMAIL: 'kali@gmail.com', PHONE_NUMBER: '+15334567890'},
      isTest: false, adgroupId : 0, creativeId: 0, status: 'OPEN', notes: 'hello'} as Lead;

      leadService.updateLead(lead).subscribe(/* Don't care about the response */);

      // expect a PUT request with all the correct parameters in the body
      const req = httpMock.expectOne(leadService.url);
      const request = req.request;
      expect(request.method).toBe('PUT');
      expect(request.body.leadId).toBe('cKlwe8W4xN4CFcoFrwkdyHEByt');
      expect(request.body.status).toBe('OPEN');
      expect(request.body.notes).toBe('hello');
    });
  });
});
