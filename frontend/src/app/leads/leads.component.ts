import { Component, OnInit } from '@angular/core';
import { LeadService } from '../shared/lead.service';
import { Lead } from './model/lead.model';
import {MatTableDataSource} from '@angular/material/table';

@Component({
  selector: 'app-leads',
  templateUrl: './leads.component.html',
  styleUrls: ['./leads.component.css']
})
export class LeadsComponent implements OnInit {
   leads : Lead[];

    displayedColumns = ['lead_id','name', 'phone_number', 'campaign_id', 'date'];
    dataSource: MatTableDataSource<Lead>;

   constructor(private leadService : LeadService) { }

     ngOnInit() : void {
        this.getAllLeads();
      }

      getAllLeads(): void {
        this.leadService.getAllLeads()
        .subscribe((leads) => {this.dataSource = new MatTableDataSource(leads); });
      }


}
