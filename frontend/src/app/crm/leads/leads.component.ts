import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';

import { LeadService } from '../../services/lead.service';

import { Lead } from '../../models/lead.model';

//Material imports
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';

import {delay} from 'rxjs/operators';

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
   dataSource : MatTableDataSource<Lead>;
   isLoading = true;

   @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
   @ViewChild(MatSort, {static: true}) sort: MatSort;

   constructor(private leadService : LeadService) { }

   ngOnInit() : void {
      delay(20000);
      this.getAllLeads();
      this.isLoading = false;
   }

   getAllLeads(): void {
     this.leadService.getAllLeads()
     .subscribe((leads) => {
       this.dataSource = new MatTableDataSource(leads);
       this.dataSource.paginator = this.paginator;
       this.dataSource.sort = this.sort;
      } );
   }
   applyFilter(event: Event) {
      //console.log(this.dataSource);
           console.log("Hello1")
          const filterValue = (event.target as HTMLInputElement).value;
          this.dataSource.filter = filterValue.trim().toLowerCase();
       console.log("Hello2")
          if (this.dataSource.paginator) {
             console.log("Hello3")
            this.dataSource.paginator.firstPage();
          }
   }


}
