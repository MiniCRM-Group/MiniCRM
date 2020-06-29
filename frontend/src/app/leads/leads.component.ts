import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';

import { LeadService } from '../shared/lead.service';

import { Lead } from './model/lead.model';

//Material imports
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';

@Component({
  selector: 'app-leads',
  templateUrl: './leads.component.html',
  styleUrls: ['./leads.component.css']
})
export class LeadsComponent implements OnInit {
    //Creates an array of Leads
    leads : Lead[];

    //Initiating dataSource for tabulating the fetched JSON
    displayedColumns = ['lead_id','name', 'phone_number', 'campaign_id', 'date'];
    dataSource: MatTableDataSource<Lead>;

   @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
   constructor(private leadService : LeadService) { }

     ngOnInit() : void {
         this.getAllLeads();
     }

      getAllLeads(): void {
         this.leadService.getAllLeads()
         .subscribe((leads) => {
         this.dataSource = new MatTableDataSource(leads);
         this.dataSource.paginator = this.paginator;

        });
      }


}
