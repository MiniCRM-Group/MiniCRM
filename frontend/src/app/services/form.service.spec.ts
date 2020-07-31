import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { FormService } from './form.service';
import { FormsResponse, Form } from '../models/server_responses/forms-response.model';

describe('FormService', () => {
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

  it('should return an Observable<FormResponse>', () => {
    const dummyFormResponse: FormsResponse = {
      forms: [
        { formId: 1, formName: 'Form 1', date: Date().toString() },
        { formId: 2, formName: 'Form 2', date: Date().toString() }
      ] as Form[]
    };

    service.getForms().subscribe((res: FormsResponse) => {
      expect(res.forms).toEqual(dummyFormResponse.forms);
    });

    const req = httpMock.expectOne(service.formEndpoint);
    expect(req.request.method).toBe('GET');
    req.flush(dummyFormResponse);
  });
});
