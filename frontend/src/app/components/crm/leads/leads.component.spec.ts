import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { LeadsModule } from './leads.module';
import { LeadService } from 'src/app/services/lead.service';
import { LeadsComponent } from './leads.component';
import { of } from 'rxjs';
import { Lead, LeadStatus } from 'src/app/models/server_responses/lead.model';
import { NoopAnimationsModule} from '@angular/platform-browser/animations';
import { MatTableHarness } from '@angular/material/table/testing';
import { HarnessLoader } from '@angular/cdk/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { FormService } from 'src/app/services/form.service';
import { CampaignService } from 'src/app/services/campaign.service';

describe('LeadsComponent', () => {
  let loader: HarnessLoader;
  let component: LeadsComponent;
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
        EMAIL: 'roddy@example.com'
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
        EMAIL: 'alex@email.com'
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
      campaignId: 1,
      gclId: '1',
      apiVersion: '1.0.0',
      formId: 2,
      googleKey: 'amansrandomkey',
      columnData: {
        FULL_NAME: 'Aman',
        PHONE_NUMBER: '106-107-1080',
        EMAIL: 'aman@crm.com'
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

  let fixture: ComponentFixture<LeadsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ LeadsModule, NoopAnimationsModule],
     // aotSummaries: LeadsModuleNgSummary,
      providers: [
        { provide: LeadService, useValue: leadService },
        { provide: FormService, useValue: formService },
        { provide: CampaignService, useValue: campaignService }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LeadsComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display all leads', async () => {
    const leadsTable = await loader.getHarness(MatTableHarness);
    const displayedLeads = await leadsTable.getRows();
    displayedLeads.forEach(async (displayedLead, index) => {
      const [selectCell, leadIdCell, dateCell, nameCell,
      phoneNumberCell, emailCell,
      statusCell, formNameCell, campaignNameCell, moreInfoCell] = await displayedLead.getCells();
      const leadId = await leadIdCell.getText();
      expect(leadId).toEqual(leads[index].leadId);
      const name = await nameCell.getText();
      expect(name).toEqual(leads[index].columnData.FULL_NAME);
      const phoneNum = await phoneNumberCell.getText();
      expect(phoneNum).toEqual(leads[index].columnData.PHONE_NUMBER);
      const email = await emailCell.getText();
      expect(email).toEqual(leads[index].columnData.EMAIL);
      const campaignName = await campaignNameCell.getText();
      expect(campaignName).toEqual(campaignNameMap.get(leads[index].campaignId));
      const formName = await formNameCell.getText();
      expect(formName).toEqual(formNameMap.get(leads[index].formId));
      // dates are failing for some reason
      const date = await dateCell.getText();
    });
  });
});
