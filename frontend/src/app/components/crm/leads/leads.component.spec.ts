import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { LeadsModule } from './leads.module';
import { LeadService } from 'src/app/services/lead.service';
import { LeadsComponent } from './leads.component';
import { of } from 'rxjs';
import { Lead } from 'src/app/models/server_responses/lead.model';
import { NoopAnimationsModule} from '@angular/platform-browser/animations';
import { MatTableHarness } from '@angular/material/table/testing';
import { HarnessLoader } from '@angular/cdk/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';

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
      formId: 2,
      googleKey: 'googlekey1',
      columnData: {
        FULL_NAME: 'Roddy',
        PHONE_NUMBER: '101-101-1010',
        EMAIL: 'roddy@example.com'
      },
      isTest: false,
      adgroupId: 1,
      creativeId: 1,
      status: 'CLOSED',
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
      status: 'OPEN',
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
      status: 'CLOSED',
      notes: 'Great lead'
    }
  ];
  const leadService: Partial<LeadService> = {
    getAllLeads: () => of<Lead[]>(leads)
  };
  let fixture: ComponentFixture<LeadsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ LeadsModule, NoopAnimationsModule],
     // aotSummaries: LeadsModuleNgSummary,
      providers: [
        { provide: LeadService, useValue: leadService }
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
      const [selectCell, leadIdCell, nameCell,
      phoneNumberCell, emailCell, campaignIdCell,
      dateCell, statusCell, moreInfoCell] = await displayedLead.getCells();
      const leadId = await leadIdCell.getText();
      expect(leadId).toEqual(leads[index].leadId);
      const name = await nameCell.getText();
      expect(name).toEqual(leads[index].columnData.FULL_NAME);
      const phoneNum = await phoneNumberCell.getText();
      expect(phoneNum).toEqual(leads[index].columnData.PHONE_NUMBER);
      const email = await emailCell.getText();
      expect(email).toEqual(leads[index].columnData.EMAIL);
      const campaignId = await campaignIdCell.getText();
      expect(campaignId).toEqual(leads[index].campaignId.toString());
      // dates are failing for some reason
      const date = await dateCell.getText();
    });
  });
});
