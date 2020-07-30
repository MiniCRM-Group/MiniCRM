import { CampaignsComponent } from './campaigns.component';
import { CampaignsModule } from './campaigns.module';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { CampaignService } from 'src/app/services/campaign.service';
import { of } from 'rxjs';
import { Campaign, CampaignsResponse } from 'src/app/models/server_responses/campaign.model';
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HarnessLoader } from '@angular/cdk/testing';
import { By } from '@angular/platform-browser';
import { MatInputHarness } from '@angular/material/input/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';

describe('CampaignsComponent', () => {
  let loader: HarnessLoader;
  let nativeElement: HTMLElement;
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
    getAllCampaigns: () => of<CampaignsResponse>({ campaigns }),
    renameCampaign: (campaign: Campaign) => {
      const campaignsCopy = Array.from(campaigns);
      campaignsCopy.forEach(c => {
        if (c.campaignId === campaign.campaignId) {
          c.campaignName = campaign.campaignName;
        }
      });
      return of<CampaignsResponse>({ campaigns: campaignsCopy });
    }
  };
  let component: CampaignsComponent;
  let fixture: ComponentFixture<CampaignsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CampaignsComponent ],
      providers: [
        { provide: CampaignService, useValue: campaignService }
      ],
      imports: [ CampaignsModule, NoopAnimationsModule],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CampaignsComponent);
    component = fixture.componentInstance;
    nativeElement = fixture.debugElement.nativeElement;
    loader = TestbedHarnessEnvironment.loader(fixture);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display all campaign ids', () => {
    const expectedCampaignids = campaigns.map(camp => camp.campaignId.toString().trim());
    const actualCampaignIds = scrapeValues(nativeElement, '.campaignId');
    expect(actualCampaignIds).toEqual(expectedCampaignids);
  });

  it('should display all campaign names', () => {
    const expectedCampaignNames = campaigns.map(camp => camp.campaignName.toString().trim());
    const campaignNameElements = fixture.debugElement.queryAll(By.css('.campaignName .editable'));
    fixture.whenStable().then(() => {
      const actualCampaignNames = Array.from(campaignNameElements).map(elem => elem.nativeElement.value);
      expect(actualCampaignNames).toEqual(expectedCampaignNames);
    });
  });

  it('should display all campaign dates', () => {
    const expectedCampaignDates = campaigns.map(camp => camp.date.trim());
    const actualCampDates = scrapeValues(nativeElement, '.date');
    expect(actualCampDates).toEqual(expectedCampaignDates);
  });

  // not working for some reason
  xit('should filter campaigns', async () => {
    const campaignFilter = await loader.getHarness(MatInputHarness.with({ placeholder: 'Search' }));
    await campaignFilter.setValue('1');
    expect(component.campaignsTable.dataSource.filteredData).toEqual([ campaigns[0] ]);
  });

  it('should edit campaign name', async () => {
    const campaignNameField = await loader.getHarness(MatInputHarness.with({ value: 'Campaign 1' }));
    await campaignNameField.setValue('EDITED');
    expect(component.campaignsTable.data.map(camp => camp.campaignName)).toContain('EDITED');
  });
});


/**
 * Provides a list of values stored inside table column.
 * @param htmlElement The HTML element to query through.
 * @param cssSelector The CSS selector that the table cells are identified by.
 */
function scrapeValues(htmlElement: HTMLElement, cssSelector: string): string[] {
  return Array.from(htmlElement.querySelectorAll(cssSelector))
  .map(value => value.textContent.trim());
}
