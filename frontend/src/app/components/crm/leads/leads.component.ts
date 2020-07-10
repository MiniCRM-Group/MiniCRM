/**
 * This typescript file is reponsible for all the features on the leads component. It is dependent on:
 *  - The lead model/interface
 *  - The lead service
 */

import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';

/**
 * Import Material components
 */
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { SelectionModel } from '@angular/cdk/collections';
import { MatDialog } from '@angular/material/dialog';


/**
 * Imports from the RxJS library
 */
import { BehaviorSubject } from 'rxjs';
import { first } from 'rxjs/operators';

import { Lead } from '../../../models/server_responses/lead.model';
import { LeadService } from '../../../services/lead.service';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-leads',
  templateUrl: './leads.component.html',
  styleUrls: ['./leads.component.css']
})

export class LeadsComponent implements AfterViewInit {
  leads: Lead[];

  readonly isLoading$ = new BehaviorSubject<boolean>(true);
  readonly dataSource: MatTableDataSource<Lead>;
  selection = new SelectionModel<Lead>(true, []);
  /**
   * Column IDs that we plan to show on the table are stored here
   */
  readonly displayedColumns: string[] = [
    'select',
    'lead_id',
    'name',
    'phone_number',
    'campaign_id',
    'date',
    'details'
  ];

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor(private readonly leadService: LeadService, public dialog: MatDialog,
    private titleService: Title) {
    this.titleService.setTitle('Leads');
    this.dataSource = new MatTableDataSource();
    this.loadAllLeads();
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;

    /**
     * This will let the dataSource sort feature to access nested properties in the JSON such as column_data.
     */
    this.dataSource.sortingDataAccessor = (lead, property) => {
      switch(property) {
        case 'name': return lead.column_data.FULL_NAME;
        case 'phone_number': return lead.column_data.PHONE_NUMBER;
        default: return lead[property];
      }
    };
    this.dataSource.sort = this.sort;
  }

 /**
  * This will access the leads from the leadService and handle the filterPredicate and the isLoading boolean value.
  */
  loadAllLeads(): void {
    this.isLoading$.next(true);
    this.leadService.getAllLeads().pipe(first()).subscribe((leads) => {
      this.dataSource.data = leads;

      /**
       * @param data The whole data we have in the JSON.
       * @param filter The value that the user searches transformedFilter.
       */
      this.dataSource.filterPredicate = (data, filter: string)  => {
        let cleanString = (str: string): string => str.trim().toLowerCase();
        let hasFilter = (data: object | number | string | boolean, filter: string): boolean => {
          // traverse through JSON's tree like structure
          if(typeof data === 'object') {
            let keys = Object.keys(data);
            for(let key in keys) {
              if(hasFilter(data[key], filter)) {
                return true;
              }
            }
          } else {
            // if you hit a key-value pair where the value is 
            // a primitve type compare and return only if filter found
            let value = cleanString(data.toString());
            if(value.indexOf(filter) !== -1) {
              return true;
            }
          }
          return false;
        };
        return hasFilter(data, cleanString(filter));
      };
      this.isLoading$.next(false);
      });
  }

 /**
  * This method will listen to the filter field in the html and update the value of dataSource
  * @param event an input from the filter field
  */
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
    if (this.dataSource.paginator != null) {
      this.dataSource.paginator.firstPage();
    }
  }

  /** Whether the number of selected elements matches the total number of rows. */
  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  masterToggle() {
    this.isAllSelected() ?
        this.selection.clear() :
        this.dataSource.data.forEach(row => this.selection.select(row));
  }

    /** The label for the checkbox on the passed row */
  checkboxLabel(row?: Lead): string {
    if (!row) {
      return `${this.isAllSelected() ? 'select' : 'deselect'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.lead_id + 1}`;
  }

  openDialog() {
      const dialogRef = this.dialog.open(DetailsDialog);

      dialogRef.afterClosed().subscribe(result => {
      });
  }

}
  @Component({
    selector: 'DetailsDialog',
    templateUrl: './leads-details.component.html',
    styleUrls: ['./leads.component.css']
  })
export class DetailsDialog {}
