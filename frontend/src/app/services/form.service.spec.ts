import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { FormService } from './form.service';
import { FormsResponse, Form } from '../models/server_responses/forms-response.model';

describe('FormService', () => {
  const dummyFormsResponse: FormsResponse = {
    forms: [
      { formId: 1, formName: 'Form 1', date: Date().toString() },
      { formId: 2, formName: 'Form 2', date: Date().toString() }
    ] as Form[]
  };

  let service: FormService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ HttpClientTestingModule ],
      providers: [ FormService ]
    });
    service = TestBed.inject(FormService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getForms', () => {
    it('should return an Observable<FormResponse>', () => {
      service.getForms().subscribe((res: FormsResponse) => {
        expect(res.forms).toEqual(dummyFormsResponse.forms);
      });

      const req = httpMock.expectOne(service.endpoint);
      expect(req.request.method).toBe('GET');
      req.flush(dummyFormsResponse);
    });
  });

  describe('renameForm', () => {
    it('should do a PUT request with the form id and name', () => {
      const form: Form = {date: Date().toString(), formId: 1, formName: 'newName' } as Form;

      service.renameForm(form).subscribe(/* Don't care about the response */);

      // expect a PUT request with all the correct parameters in the body
      const req = httpMock.expectOne(service.endpoint);
      const request = req.request;
      expect(request.method).toBe('PUT');
      expect(request.body.formId).toBe('1');
      expect(request.body.formName).toBe('newName');
    });
  });

  describe('getFormNameMap', () => {
    it('should return a map containing all form ids mapped to correct names', () => {
      const expectedMap: Map<number, string> = new Map();
      expectedMap.set(1, 'Form 1');
      expectedMap.set(2, 'Form 2');

      service.getFormNameMap().subscribe((map: Map<number, string>) => {
        expect(map).toEqual(expectedMap);
      });

      const req = httpMock.expectOne(service.endpoint);
      const request = req.request;
      expect(request.method).toBe('GET');
      req.flush(dummyFormsResponse);
    });

    it('should return an empty map with no forms', () => {
      const expectedMap: Map<number, string> = new Map();

      service.getFormNameMap().subscribe((map: Map<number, string>) => {
        expect(map).toEqual(expectedMap);
      });

      const req = httpMock.expectOne(service.endpoint);
      const request = req.request;
      expect(request.method).toBe('GET');
      req.flush({forms: [] as Form[]});
    });
  });
});
