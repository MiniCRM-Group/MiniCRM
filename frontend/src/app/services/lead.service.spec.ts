import { TestBed } from '@angular/core/testing';

import { LeadService } from './lead.service';
import { Lead } from '../models/server_responses/lead.model';

import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';

describe('LeadService', () => {
  let apiService: LeadService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ HttpClientTestingModule ],
      providers: [ LeadService ]
    });
    apiService = TestBed.inject(LeadService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should retrieve all leads  from the API via GET', () => {
  	const fakeLeads: Lead[] = [
	  	{
	  		lead_id:"dKlwe8W4xN4CFcoFrwkdyHEByt",
	  	    campaign_id: 304256,
	        gcl_id: "qebai8W4xN4CFcoFrwkdyHEByw",
	  user_column_data: [
	  //lead1
	    {
	      colum_name:"User Name",
	      string_value:"Adli Muli",
	      column_id: "FULL_NAME"
	    },
	    {
	      column_name: "User Phone",
	      string_value:"+95334567890",
	      column_id:"PHONE_NUMBER"
	    },
	    {
	      column_name: "User Email",
	      string_value:"ali@gmail.com",
	      column_id:"EMAIL"
	    }
	  ],
	  api_version:"1.0",
	  form_id:9574,
	  google_key: hWDxdHeMT1yq9DZgt8Io,
	  adgroup_id:0,
	  creative_id :0,
	  status: "OPEN"		
	  	},
	  //lead2
	  	{
	  		lead_id:"qKlwe8W4xN4CFcoFrwkdyHEByt",
	  	    campaign_id: 304256,
	        gcl_id: "cebai8W4xN4CFcoFrwkdyHEByw",
	  user_column_data: [
	    {
	      colum_name:"User Name",
	      string_value:"Abli Mulik",
	      column_id: "FULL_NAME"
	    },
	    {
	      column_name: "User Phone",
	      string_value:"+15334567890",
	      column_id:"PHONE_NUMBER"
	    },
	    {
	      column_name: "User Email",
	      string_value:"abli@gmail.com",
	      column_id:"EMAIL"
	    }
	  ],
	  api_version:"1.0",
	  form_id:9574,
	  google_key: hWDxdHeMT1yq9DZgt8Io,
	  adgroup_id:0,
	  creative_id :0,
	  status: "OPEN"		
  	},
  	];
    
    // act 
  	apiService.getAllLeads()._subscribe(leads => {
  		expect(leads.length).toBe(2);
  		expect(leads).toEqual(fakeLeads);
  	});
    
    // http mock
  	const request = httpMock.expectOne('${apiService.url}'); 
  	expect(request.request.method).toBe('GET');

  	request.flush(fakeLeads);
  });


  it('should be created', () => {
    expect(apiService).toBeTruthy();
  });
});
