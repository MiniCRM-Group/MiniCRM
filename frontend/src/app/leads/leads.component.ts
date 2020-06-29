import { Component, OnInit } from '@angular/core';
import { LeadService } from '../shared/lead.service';
import { Lead } from './model/lead.model';

@Component({
  selector: 'app-leads',
  templateUrl: './leads.component.html',
  styleUrls: ['./leads.component.css']
})
export class LeadsComponent implements OnInit {
   leads : Lead[];

   constructor(private leadService : LeadService) { }

     ngOnInit() : void {
        this.getAllLeads();
      }

      getAllLeads(): void {
        this.leadService.getAllLeads()
        .subscribe(leads => this.leads = leads);
      }


}
