import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { LeadService } from '../shared/lead.service';
import { Lead } from './model/lead.model';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';

@Component({
  selector: 'app-leads',
  templateUrl: './leads.component.html',
  styleUrls: ['./leads.component.css']
})
export class LeadsComponent implements OnInit {
   leads : Lead[];

    displayedColumns = ['lead_id','name', 'phone_number', 'campaign_id', 'date'];
    dataSource: MatTableDataSource<Lead>;

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
   constructor(private leadService : LeadService) { }

     ngOnInit() : void {
         this.getAllLeads();
  //   setTimeout(() => this.dataSource.paginator = this.paginator);
      }

      getAllLeads(): void {
        this.leadService.getAllLeads()
        .subscribe((leads) => {
        this.dataSource = new MatTableDataSource(leads);
         this.dataSource.paginator = this.paginator;
        });
      }


}
