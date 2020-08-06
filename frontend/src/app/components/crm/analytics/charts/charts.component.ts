import { Component, OnInit } from '@angular/core';
import { LeadService } from 'src/app/services/lead.service';
import { Lead, LeadStatus } from 'src/app/models/server_responses/lead.model';
import { HumanizeEnumPipe } from 'src/app/pipes/humanize-enum.pipe';
import { CampaignService } from 'src/app/services/campaign.service';
import { FormService } from 'src/app/services/form.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-charts',
  templateUrl: './charts.component.html',
  styleUrls: ['./charts.component.css']
})
export class ChartsComponent implements OnInit {
  pipe: HumanizeEnumPipe = new HumanizeEnumPipe();
  leads: Lead[];
  leadsByStatus: object[];
  leadsByForm: object[];
  leadsByCampaign: object[];
  convertedLeads: object[];

  isLoading: boolean;
  /**
   * Maps formId to formName
   */
  formNameMap: Map<number, string>;
  /**
   * Maps campaignId to campaignName
   */
  campaignNameMap: Map<number, string>;

  constructor(private readonly leadService: LeadService,
              private readonly formService: FormService,
              private readonly campaignService: CampaignService) {
  }

  ngOnInit(): void {
    this.isLoading = true;
    forkJoin([
      this.formService.getFormNameMap(),
      this.campaignService.getCampaignNameMap(),
      this.leadService.getAllLeads()
    ]).subscribe(t => {
      this.formNameMap = t[0];
      this.campaignNameMap = t[1];
      this.leads = t[2];
      this.generateData();
      this.isLoading = false;
    });
  }

  generateData(): void {
    // fill out the leadsByStatus object
    const statusMap: Map<LeadStatus, number> = new Map<LeadStatus, number>();
    for (const lead of this.leads) {
      if (statusMap.has(lead.status)) {
        statusMap.set(lead.status, statusMap.get(lead.status) + 1);
      } else {
        statusMap.set(lead.status, 1);
      }
    }
    this.leadsByStatus = [];
    statusMap.forEach((count: number, key: LeadStatus) => {
      const dataPoint = {
        name: this.pipe.transform(LeadStatus[key]),
        value: count
      };
      this.leadsByStatus.push(dataPoint);
    });

    // fill out the leadsByForm object
    const formMap: Map<number, number> = new Map<number, number>();
    for (const lead of this.leads) {
      if (formMap.has(lead.formId)) {
        formMap.set(lead.formId, formMap.get(lead.formId) + 1);
      } else {
        formMap.set(lead.formId, 1);
      }
    }
    this.leadsByForm = [];
    formMap.forEach((count: number, formId: number) => {
      const dataPoint = {
        name: this.formNameMap.get(formId),
        value: count
      };
      this.leadsByForm.push(dataPoint);
    });

    // fill out the leadsByCampaign object
    const campaignMap: Map<number, number> = new Map<number, number>();
    for (const lead of this.leads) {
      if (campaignMap.has(lead.campaignId)) {
        campaignMap.set(lead.campaignId, campaignMap.get(lead.campaignId) + 1);
      } else {
        campaignMap.set(lead.campaignId, 1);
      }
    }
    this.leadsByCampaign = [];
    campaignMap.forEach((count: number, campaignId: number) => {
      const dataPoint = {
        name: this.campaignNameMap.get(campaignId),
        value: count
      };
      this.leadsByCampaign.push(dataPoint);
    });

    // fill out the convertedLeads object
    const converted = {
      name: 'Converted',
      value: statusMap.get(LeadStatus.CLOSED_CONVERTED)
    };
    this.convertedLeads = [converted];
  }
}
