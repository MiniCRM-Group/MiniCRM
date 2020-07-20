import { Component, OnInit, Input, ChangeDetectionStrategy, OnChanges, AfterContentChecked, ChangeDetectorRef } from '@angular/core';
import { TableData, KeyDisplayedNameMap } from '../../../models/component_states/table-data.model';
import * as _ from 'lodash';
import { MatTableDataSource } from '@angular/material/table';
import { SelectionModel } from '@angular/cdk/collections';
import { Observable, of } from 'rxjs';

@Component({
  selector: 'app-crm-table',
  templateUrl: './crm-table.component.html',
  styleUrls: ['./crm-table.component.css']
})
export class CrmTableComponent<T> implements OnInit {
  @Input() data: Observable<T[]>;
  @Input() keyOrdering: string[] = [];
  @Input() selectionEnabled = true;
  dataSource: MatTableDataSource<T> = new MatTableDataSource<T>();
  keyDisplayedNameOrdering: KeyDisplayedNameMap[];
  public selection: SelectionModel<T> = new SelectionModel<T>(/*allow mulitselect=*/true);

  constructor(private changeDectectorRef: ChangeDetectorRef) {
  }


  ngOnInit(): void {
    // we can expect inputs here but not in constructor
    this.keyDisplayedNameOrdering = this.keyOrdering.map((key: string) => {
      return { key, displayedName: _.startCase(key) } as KeyDisplayedNameMap;
    });
    if (this.selectionEnabled) {
      // adds selection column
      this.keyOrdering.unshift('select');
    }
    this.refreshDataSource();
  }

  public refreshDataSource(): void {
    this.data.subscribe((res: T[]) => {
      this.dataSource.data = res;
      this.changeDectectorRef.detectChanges();
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
}
