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

import { Lead } from '../../models/lead.model';
import { LeadService } from '../../services/lead.service';

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

  constructor(private readonly leadService: LeadService, public dialog: MatDialog) {
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
        const accumulator = (currentTerm, key) => {
          //Using the recusrsive method here becuase using an if/else condition will increase space complexity n times.
          return this.nestedPropertyFilterCheck(currentTerm, data, key);
        };
        const dataStr = Object.keys(data).reduce(accumulator, '').toLowerCase();

        /**
         * Transform the filter by converting it to lowercase and removing whitespace.
         */
        const transformedFilter = filter.trim().toLowerCase();
        return dataStr.indexOf(transformedFilter) !== -1;
      };
      this.isLoading$.next(false);
      });
  }

  /**
   * A Recursive function to check for each data in the nested JSON
   * @param leadString A lead that exists in the JSON
   * @param data The whole data we have in the JSON
   * @param key The property of the lead at is nested if it exists
   * @return leadString
   */
  nestedPropertyFilterCheck(leadString, data, key) {
    if (typeof data[key] === 'object') {
      for (const k in data[key]) {
        if (data[key][k] !== null) {
            leadString = this.nestedPropertyFilterCheck(leadString, data[key], k);
          }
        }
    } else {
        leadString += data[key];
    }
      return leadString;
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
