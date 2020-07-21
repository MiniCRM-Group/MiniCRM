import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CampaignsComponent } from './campaigns.component';
import { CampaignService } from 'src/app/services/campaign.service';
import { of } from 'rxjs';
import { Campaign } from 'src/app/models/server_responses/campaign.model';

describe('CampaignsComponent', () => {
  const campaignService: Partial<CampaignService> = {
    getAllCampaigns: () => of<Campaign[]>([])
  };
  let component: CampaignsComponent;
  let fixture: ComponentFixture<CampaignsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CampaignsComponent ],
      providers: [
        { provide: CampaignService, useValue: campaignService }
      ]
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
});
