import { Component, OnInit } from '@angular/core';
import { LeadService } from 'src/app/services/lead.service';
import { Lead } from 'src/app/models/server_responses/lead.model';

@Component({
  selector: 'app-charts',
  templateUrl: './charts.component.html',
  styleUrls: ['./charts.component.css']
})
export class ChartsComponent implements OnInit {
  leads: Lead[];
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
      this.isLoading = false;
    });
  }
}
