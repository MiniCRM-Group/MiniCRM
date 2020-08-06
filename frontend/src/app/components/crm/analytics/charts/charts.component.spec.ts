import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChartsComponent } from './charts.component';
import { Lead, LeadStatus } from 'src/app/models/server_responses/lead.model';
import { LeadService } from 'src/app/services/lead.service';
import { of } from 'rxjs';
import { FormService } from 'src/app/services/form.service';
import { CampaignService } from 'src/app/services/campaign.service';

describe('ChartsComponent', () => {
  let component: ChartsComponent;
  const leads: Lead[] = [
    {
      date: new Date(2020, 7, 30, 12, 40, 1, 0),
      leadId: '1',
      campaignId: 1,
      gclId: '1',
      apiVersion: '1.0.0',
      formId: 1,
      googleKey: 'googlekey1',
      columnData: {
        FULL_NAME: 'Roddy',
        PHONE_NUMBER: '101-101-1010',
        EMAIL: 'roddy@example.com',
        POSTAL_CODE: '43004',
        STREET_ADDRESS: '3975 Townsfair Way',
        CITY: 'Columbus',
        REGION: 'OH',
        COUNTRY: 'US'
      },
      isTest: false,
      adgroupId: 1,
      creativeId: 1,
      status: LeadStatus.CLOSED_CONVERTED,
      notes: 'Linked us with more potential clients'
    },
    {
      date: new Date(2021, 7, 30, 12, 40, 1, 0),
      leadId: '2',
      campaignId: 1,
      gclId: '1',
      apiVersion: '1.0.0',
      formId: 2,
      googleKey: 'keyforAlex',
      columnData: {
        FULL_NAME: 'Alex',
        PHONE_NUMBER: '201-301-4010',
        EMAIL: 'alex@email.com',
        POSTAL_CODE: '43004',
        STREET_ADDRESS: '3975 Townsfair Way',
        CITY: 'Columbus',
        REGION: 'OH',
        COUNTRY: 'US'
      },
      isTest: false,
      adgroupId: 1,
      creativeId: 1,
      status: LeadStatus.OPEN,
      notes: 'Awesome returns'
    },
    {
      date: new Date(2022, 7, 30, 12, 40, 1, 0),
      leadId: '3',
      campaignId: 2,
      gclId: '1',
      apiVersion: '1.0.0',
      formId: 2,
      googleKey: 'amansrandomkey',
      columnData: {
        FULL_NAME: 'Aman',
        PHONE_NUMBER: '106-107-1080',
        EMAIL: 'aman@crm.com',
        POSTAL_CODE: '43004',
        STREET_ADDRESS: '3975 Townsfair Way',
        CITY: 'Columbus',
        REGION: 'OH',
        COUNTRY: 'US'
      },
      isTest: false,
      adgroupId: 1,
      creativeId: 1,
      status: LeadStatus.CLOSED_NOT_CONVERTED,
      notes: 'Great lead'
    }
  ];
  const leadService: Partial<LeadService> = {
    getAllLeads: () => of<Lead[]>(leads)
  };

  const formNameMap: Map<number, string> = new Map();
  formNameMap.set(1, 'Form 1');
  formNameMap.set(2, 'Form 2');
  const formService: Partial<FormService> = {
    getFormNameMap: () => of<Map<number, string>>(formNameMap)
  };

  const campaignNameMap: Map<number, string> = new Map();
  campaignNameMap.set(1, 'Campaign 1');
  campaignNameMap.set(2, 'Campaign 2');
  const campaignService: Partial<CampaignService> = {
    getCampaignNameMap: () => of<Map<number, string>>(campaignNameMap)
  };

  let fixture: ComponentFixture<ChartsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChartsComponent ],
      providers: [
        { provide: LeadService, useValue: leadService },
        { provide: FormService, useValue: formService },
        { provide: CampaignService, useValue: campaignService }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChartsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
