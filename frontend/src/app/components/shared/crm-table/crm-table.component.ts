import { Component, OnInit, Input } from '@angular/core';
import { NameMap, Table, TableInput, TableRow, TableRowInput } from '../../../models/component_states/table.model';

@Component({
  selector: 'app-crm-table',
  templateUrl: './crm-table.component.html',
  styleUrls: ['./crm-table.component.css']
})
export class CrmTableComponent implements OnInit {
  @Input() private dataSource: TableInput;
  @Input() private nameMaps?: NameMap[];
  @Input() private selectionEnabled?: boolean;
  dataSourceTable: Table;
  displayedColumnNames: string[];
  constructor() { }

  ngOnInit(): void {
    if(this.dataSource.length > 0) {
      this.dataSourceTable = {
        tableRows: this.getDefaultTableRows(),
        nameMaps: this.nameMaps ?? this.getDefaultNameMaps(),
        selectAll: this.selectAll,
        getSelected: () => [],
        filter: (a, b) => [],
        sort: (a, b) => [],
      };
      this.displayedColumnNames = this.dataSourceTable
      .nameMaps.map((nm: NameMap) => {
        return nm.columnName; 
      });
      if(this.selectionEnabled ?? false) {
        this.displayedColumnNames.unshift('isSelected')
      }
    } else {
      // show a message saying there is nothing to display
    }
  }

  private getDefaultTableRows(): TableRow[] {
    return this.dataSource.map((datum: TableRowInput) => {
      return {
        datum: datum,
        isSelected: false // by default no row is selected
      }
    });
  }

  private getDefaultNameMaps(): NameMap[] {
    let keys: string[] = Object.keys(this.dataSource[0]);
    let defaultNameMaps: NameMap[] = keys.map((key: string) => {
      return {
        columnName: key,
        displayedColumnName: key
      }
    });
    console.log(defaultNameMaps);
    return defaultNameMaps;
  }

  private selectAll(): void {
    this.dataSourceTable.tableRows.forEach((tr: TableRow) => {
      tr.isSelected = true;
    });
  }
}
