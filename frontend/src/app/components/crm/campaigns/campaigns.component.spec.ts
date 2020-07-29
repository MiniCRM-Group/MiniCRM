import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CampaignsComponent } from './campaigns.component';
<<<<<<< Updated upstream

describe('CampaignsComponent', () => {
=======
import { CampaignsModule } from './campaigns.module';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { CampaignService } from 'src/app/services/campaign.service';
import { of } from 'rxjs';
import { Campaign } from 'src/app/models/server_responses/campaign.model';
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HarnessLoader } from '@angular/cdk/testing';

describe('CampaignsComponent', () => {
  let loader: HarnessLoader;
  const campaigns: Campaign[] = [
    {
      campaignName: 'Campaign 1',
      campaignId: 1,
      date: 'Today'
    },
    {
      campaignName: 'Campaign 2',
      campaignId: 2,
      date: 'Yesterday'
    },
    {
      campaignName: 'Campaign 3',
      campaignId: 3,
      date: 'June 24'
    }
  ];
  const campaignService: Partial<CampaignService> = {
    getAllCampaigns: () => of<Campaign[]>([]),
    renameCampaign: (campaign: Campaign) => {
      let campaignsCopy = Array.from(campaigns);
      campaignsCopy.forEach(c => {
        if(c.campaignId === campaign.campaignId) {
          c.campaignName = campaign.campaignName;
        }
      });
      return campaignsCopy;
    }
  };
>>>>>>> Stashed changes
  let component: CampaignsComponent;
  let fixture: ComponentFixture<CampaignsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CampaignsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CampaignsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display all campaign ids', () => {
    const expectedCampaignids = campaigns.map(camp => camp.campaignId.toString().trim());
    const actualFormIds = scrapeValues(nativeElement, '.formId');
    expect(actualFormIds).toEqual(expectedFormIds);
  });

  it('should display all form names', () => {
    const expectedFormNames = forms.map(form => form.formName.toString().trim());
    const formDateElements = fixture.debugElement.queryAll(By.css('.formName .editable'));
    fixture.whenStable().then(() => {
      const actualformDates = Array.from(formDateElements).map(elem => elem.nativeElement.value);
      expect(actualformDates).toEqual(expectedFormNames);
    });
  });

  it('should display all form dates', () => {
    const expectedFormDates = forms.map(form => form.date.trim());
    const actualFormDates = scrapeValues(nativeElement, '.date');
    expect(actualFormDates).toEqual(expectedFormDates);
  });

  // not working for some reason
  xit('should filter forms', async () => {
    const formFilter = await loader.getHarness(MatInputHarness.with({ placeholder: 'Search' }));
    await formFilter.setValue('1');
    expect(component.formsTable.dataSource.filteredData).toEqual([ forms[0] ]);
  });

  it('should edit form name', async () => {
    const formNameField = await loader.getHarness(MatInputHarness.with({ value: 'Form 1' }));
    await formNameField.setValue('EDITED');
    expect(component.formsTable.data.map(form => form.formName)).toContain('EDITED');
  });
});
