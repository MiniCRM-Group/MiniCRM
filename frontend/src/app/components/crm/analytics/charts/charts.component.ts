import { Component, OnInit } from '@angular/core';
import { LeadService } from 'src/app/services/lead.service';
import { Lead, LeadStatus } from 'src/app/models/server_responses/lead.model';

@Component({
  selector: 'app-charts',
  templateUrl: './charts.component.html',
  styleUrls: ['./charts.component.css']
})
export class ChartsComponent implements OnInit {
  leads: Lead[];
  leadStatuses: object[];
  view: any[] = [700, 400];
  colorScheme = {
    domain: ['#5AA454', '#A10A28', '#C7B42C', '#AAAAAA']
  };
  isLoading: boolean;

  constructor(private leadService: LeadService) { 
    this.loadAllLeads();
  }

  ngOnInit(): void {
  }

  /**
   * This will access the leads from the leadService and handle the filterPredicate and the isLoading boolean value.
   */
  loadAllLeads(): void {
    this.isLoading = true;
    this.leadService.getAllLeads().subscribe((leads) => {
      this.leads = leads;
      this.generateData();
      this.isLoading = false;
    });
  }

  generateData(): void {
    // fill out the leadStatuses object
    const statusMap: Map<LeadStatus, number> = new Map<LeadStatus, number>();
    for (const lead of this.leads) {
      if (statusMap.has(lead.status)) {
        statusMap.set(lead.status, statusMap.get(lead.status) + 1);
      } else {
        statusMap.set(lead.status, 1);
      }
    }
    this.leadStatuses = [];
    statusMap.forEach((count: number, key: LeadStatus) => {
      const dataPoint = {
        name: LeadStatus[key],
        value: count
      };
      this.leadStatuses.push(dataPoint);
    });
  }
}
