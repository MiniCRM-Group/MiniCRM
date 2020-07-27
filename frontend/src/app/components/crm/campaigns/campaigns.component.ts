import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Observable } from 'rxjs';
import { Campaign } from 'src/app/models/server_responses/campaign.model';
import { CampaignService } from 'src/app/services/campaign.service';
import { CrmTableComponent } from '../../shared/crm-table/crm-table.component';
import { KeyDisplayedNameMap } from 'src/app/models/component_states/table-data.model';


@Component({
  selector: 'app-campaigns',
  templateUrl: './campaigns.component.html',
  styleUrls: ['./campaigns.component.css']
})
export class CampaignsComponent implements OnInit, AfterViewInit {
  @ViewChild('campaignsCrmTable') campaignsTable: CrmTableComponent<Campaign>;
  keyDisplayNameMaps: KeyDisplayedNameMap[] = [
    {
      key: 'campaignId',
      displayedName: $localize`Campaign Id`
    },
    {
      key: 'campaignName',
      displayedName: $localize`Campaign Name`
    },
    {
      key: 'date',
      displayedName: $localize`Date`
    }
  ];

  constructor(private campaignService: CampaignService,
              private titleService: Title) {
    this.titleService.setTitle($localize`Campaigns`);
  }

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
    this.campaignService.getAllCampaigns().subscribe((res: Campaign[]) => {
      this.campaignsTable.data = res;
      this.campaignsTable.refreshDataSource();
    })
  }

  /**
   * Renames the given campaign based on the id to the current name in the campaign object.
   * Called by the rename event from app-crm-table
   * @param campaign the campaign to be renamed
   */
  renameCampaign(campaign: Campaign) {
    this.campaignService.renameCampaign(campaign).subscribe();
  }

}
