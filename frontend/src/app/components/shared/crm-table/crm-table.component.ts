import { Component, OnInit, Input, ChangeDetectionStrategy } from '@angular/core';
import { TableData, KeyDisplayedNameMap } from '../../../models/component_states/table-data.model';
import * as _ from 'lodash';

@Component({
  selector: 'app-crm-table',
  templateUrl: './crm-table.component.html',
  styleUrls: ['./crm-table.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CrmTableComponent<T> implements OnInit {
  @Input() tableData: TableData<T>;
  isEmpty: boolean = false;

  constructor() {
  }

  private getDefaultNameMaps(): KeyDisplayedNameMap[] {
    let keys: string[] = this.tableData.keyOrdering ?? Object.keys(this.tableData.dataSource.data[0]);
    let defaultMaps: KeyDisplayedNameMap[] = keys.map((key: string) => {
      return {
        key: key,
        displayedName: _.startCase(key)
      }
    });
    return defaultMaps;
  }

  ngOnInit(): void {
    if(this.tableData.dataSource.data.length > 0) {
      if(this.tableData.keyDisplayedNameOrdering === undefined) {
        this.tableData.keyDisplayedNameOrdering = this.getDefaultNameMaps();
      }
      if(this.tableData.keyOrdering === undefined) {
        this.tableData.keyOrdering = this.tableData.keyDisplayedNameOrdering.map(map => map.key);
      }
      if(this.tableData.selection !== undefined) {
        this.tableData.keyOrdering.unshift('select');
      }
    } else {
      this.isEmpty = true;
    }
  }

  /** Whether the number of selected elements matches the total number of rows. */
  isAllSelected() {
    const numSelected = this.tableData.selection.selected.length;
    const numRows = this.tableData.dataSource.data.length;
    return numSelected == numRows;
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  masterToggle() {
    this.isAllSelected() ?
        this.tableData.selection.clear() :
        this.tableData.dataSource.data.forEach(row => this.tableData.selection.select(row));
  }

  // ngOnInit(): void {
  //   if(this.dataSource.length > 0) {
  //     this.dataSourceTable = {
  //       dataSource: this.getDefaultTableRows(),
  //       nameMaps: this.nameMaps ?? this.getDefaultNameMaps(),
  //       selectAll: this.selectAll,
  //       getSelected: () => [],
  //       filter: (a, b) => [],
  //       sort: (a, b) => [],
  //     };
  //     this.displayedColumnNames = this.dataSourceTable
  //     .nameMaps.map((nm: NameMap) => {
  //       return nm.columnName; 
  //     });
  //     if(this.selectionEnabled ?? false) {
  //       this.displayedColumnNames.unshift('isSelected')
  //     }
  //   } else {
  //     // show a message saying there is nothing to display
  //     this.isEmpty = true;
  //   }
  // }
}
