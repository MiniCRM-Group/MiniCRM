import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {BehaviorSubject} from 'rxjs';
import {first} from 'rxjs/operators';

import {Lead} from '../../models/lead.model';
import {LeadService} from '../../services/lead.service';

@Component({
  selector: 'app-leads',
  templateUrl: './leads.component.html',
  styleUrls: ['./leads.component.css']
})
export class LeadsComponent implements AfterViewInit {
  leads: Lead[];

  readonly isLoading$ = new BehaviorSubject<boolean>(true);
  readonly dataSource: MatTableDataSource<Lead>;
  readonly displayedColumns: string[] = [
    'lead_id',
    'name',
    'phone_number',
    'campaign_id',
    'date',
  ];

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor(private readonly leadService: LeadService) {
    this.dataSource = new MatTableDataSource();

    this.loadAllLeads();
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  loadAllLeads(): void {
    this.isLoading$.next(true);
    this.leadService.getAllLeads().pipe(first()).subscribe((leads) => {
      this.dataSource.data = leads;
      this.isLoading$.next(false);
    });
  }
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
    if (this.dataSource.paginator != null) {
      this.dataSource.paginator.firstPage();
    }
  }


}
