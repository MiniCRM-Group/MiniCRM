import { Component, OnInit, ViewChild } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Observable } from 'rxjs';
import { Campaign } from 'src/app/models/server_responses/campaign.model';
import { CampaignService } from 'src/app/services/campaign.service';
import { CrmTableComponent } from '../../shared/crm-table/crm-table.component';


@Component({
  selector: 'app-campaigns',
  templateUrl: './campaigns.component.html',
  styleUrls: ['./campaigns.component.css']
})
export class CampaignsComponent implements OnInit {
  @ViewChild('campaignsCrmTable') campaignsTable: CrmTableComponent<Campaign>;
  campaigns: Observable<Campaign[]> = this.campaignService.getAllCampaigns();
  keyOrdering: string[] = ['campaignId', 'campaignName', 'date'];

  constructor(private campaignService: CampaignService,
              private titleService: Title) {
    this.titleService.setTitle('Campaigns');
  }

  ngOnInit(): void {
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
