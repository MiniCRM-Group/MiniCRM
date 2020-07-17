import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Observable } from 'rxjs';
import { Campaign } from 'src/app/models/server_responses/campaign.model';
import { CampaignService } from 'src/app/services/campaign.service';

@Component({
  selector: 'app-campaigns',
  templateUrl: './campaigns.component.html',
  styleUrls: ['./campaigns.component.css']
})
export class CampaignsComponent implements OnInit {
  campaigns: Observable<Campaign[]> = this.campaignService.getAllCampaigns();
  
  constructor(private campaignService : CampaignService, 
    private titleService: Title) {
    this.titleService.setTitle('Campaigns');
  }

  ngOnInit(): void {
  }

}
