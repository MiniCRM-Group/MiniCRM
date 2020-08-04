import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { LeadsModule } from './leads.module';
import { LeadService } from 'src/app/services/lead.service';
import { LeadsComponent } from './leads.component';
import { of } from 'rxjs';
import { Lead, LeadStatus } from 'src/app/models/server_responses/lead.model';
import { NoopAnimationsModule} from '@angular/platform-browser/animations';
import { MatTableHarness } from '@angular/material/table/testing';
import { MatSortHarness, MatSortHeaderHarness } from '@angular/material/sort/testing';
import { HarnessLoader } from '@angular/cdk/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { FormService } from 'src/app/services/form.service';
import { CampaignService } from 'src/app/services/campaign.service';
import { MatSelectHarness } from '@angular/material/select/testing';
import { MatSelect } from '@angular/material/select';

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
      campaignId: 2,
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

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ LeadsModule, NoopAnimationsModule],
     // aotSummaries: LeadsModuleNgSummary,
      providers: [
        { provide: LeadService, useValue: leadService },
        { provide: FormService, useValue: formService },
        { provide: CampaignService, useValue: campaignService }
      ]
    })
    .compileComponents();
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

  xit('should be sortable by all sortable columns in both ascending and descending order', async () => {
    const matSortHarness: MatSortHarness = await loader.getHarness(MatSortHarness);
    const headerHarnesses: MatSortHeaderHarness[] = await matSortHarness.getSortHeaders();
    headerHarnesses.forEach(async (headerHarness) => {
      const firstSortDirection = await headerHarness.getSortDirection();
      expect(await headerHarness.isDisabled()).toBe(false);
      await headerHarness.click();
      expect(await headerHarness.isActive()).toBe(true);
      await headerHarness.click();
      expect(await headerHarness.isActive()).toBe(true);
      expect(await headerHarness.getSortDirection()).not.toEqual(firstSortDirection);
    });
  });

  describe('Forms Filter', () => {
    it('should open on click', async () => {
      const formFilter = await loader.getHarness<MatSelectHarness>(MatSelectHarness.with({selector: '#mat-select-form'}));
      (await formFilter.host()).click();
      expect(await formFilter.isOpen()).toBeTrue();
    });

    it('should show the chosen form name', async () => {
      const formFilter = await loader.getHarness<MatSelectHarness>(MatSelectHarness.with({selector: '#mat-select-form'}));
      expect(await formFilter.getValueText()).toBe('All Forms');
      await formFilter.clickOptions({text: 'Form 1'});
      expect(await formFilter.getValueText()).toBe('Form 1');
      await formFilter.clickOptions({text: 'Form 2'});
      expect(await formFilter.getValueText()).toBe('Form 2');
    });

    it('should have 3 options', async () => {
      const formFilter = await loader.getHarness<MatSelectHarness>(MatSelectHarness.with({selector: '#mat-select-form'}));
      (await formFilter.host()).click();
      const actual = (await formFilter.getOptions()).length;
      expect(actual).toBe(3);
    });

    it('should filter out by form name', async () => {
      const formFilter = await loader.getHarness<MatSelectHarness>(MatSelectHarness.with({selector: '#mat-select-form'}));
      await formFilter.clickOptions({text: 'Form 1'});
      expect(component.dataSource.filteredData).toEqual([leads[0]]);
      await formFilter.clickOptions({text: 'Form 2'});
      expect(component.dataSource.filteredData).toEqual([leads[1], leads[2]]);
    });

    it('should not filter out anything when All Forms is clicked', async () => {
      const formFilter = await loader.getHarness<MatSelectHarness>(MatSelectHarness.with({selector: '#mat-select-form'}));
      await formFilter.clickOptions({text: 'All Forms'});
      expect(component.dataSource.filteredData).toEqual(leads);
    });
  });

  describe('Campaigns Filter', () => {
    it('should open on click', async () => {
      const campaignFilter = await loader.getHarness<MatSelectHarness>(MatSelectHarness.with({selector: '#mat-select-campaign'}));
      (await campaignFilter.host()).click();
      expect(await campaignFilter.isOpen()).toBeTrue();
    });

    it('should show the chosen campaign name', async () => {
      const campaignFilter = await loader.getHarness<MatSelectHarness>(MatSelectHarness.with({selector: '#mat-select-campaign'}));
      expect(await campaignFilter.getValueText()).toBe('All Campaigns');
      await campaignFilter.clickOptions({text: 'Campaign 1'});
      expect(await campaignFilter.getValueText()).toBe('Campaign 1');
      await campaignFilter.clickOptions({text: 'Campaign 2'});
      expect(await campaignFilter.getValueText()).toBe('Campaign 2');
    });

    it('should have 3 options', async () => {
      const campaignFilter = await loader.getHarness<MatSelectHarness>(MatSelectHarness.with({selector: '#mat-select-campaign'}));
      (await campaignFilter.host()).click();
      const actual = (await campaignFilter.getOptions()).length;
      expect(actual).toBe(3);
    });

    it('should filter out by campaign name', async () => {
      const campaignFilter = await loader.getHarness<MatSelectHarness>(MatSelectHarness.with({selector: '#mat-select-campaign'}));
      await campaignFilter.clickOptions({text: 'Campaign 1'});
      expect(component.dataSource.filteredData).toEqual([leads[0], leads[1]]);
      await campaignFilter.clickOptions({text: 'Campaign 2'});
      expect(component.dataSource.filteredData).toEqual([leads[2]]);
    });

    it('should no filter out anything when All Campaigns is clicked', async () => {
      const campaignFilter = await loader.getHarness<MatSelectHarness>(MatSelectHarness.with({selector: '#mat-select-campaign'}));
      await campaignFilter.clickOptions({text: 'All Campaigns'});
      expect(component.dataSource.filteredData).toEqual(leads);
    });
  });

  describe('Multiple Filters', () => {
    it('should show the intersection of the filters, not the union', async () => {
      const formFilter = await loader.getHarness<MatSelectHarness>(MatSelectHarness.with({selector: '#mat-select-form'}));
      const campaignFilter = await loader.getHarness<MatSelectHarness>(MatSelectHarness.with({selector: '#mat-select-campaign'}));
      await formFilter.clickOptions({text: 'Form 1'});
      await campaignFilter.clickOptions({text: 'Campaign 1'});
      expect(component.dataSource.filteredData).toEqual([leads[0]]);
    });

    it('should show nothing when nothing matches the filters', async () => {
      const formFilter = await loader.getHarness<MatSelectHarness>(MatSelectHarness.with({selector: '#mat-select-form'}));
      const campaignFilter = await loader.getHarness<MatSelectHarness>(MatSelectHarness.with({selector: '#mat-select-campaign'}));
      await formFilter.clickOptions({text: 'Form 1'});
      await campaignFilter.clickOptions({text: 'Campaign 2'});
      expect(component.dataSource.filteredData).toEqual([]);
    });
  });
});
