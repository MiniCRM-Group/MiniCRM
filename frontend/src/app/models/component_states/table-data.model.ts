import { MatTableDataSource } from '@angular/material/table';
import { SelectionModel } from '@angular/cdk/collections';

export interface KeyDisplayedNameMap {
    /** Used in table data source objects. */
    key: string;
    /** Displayed in table header. */
    displayedName: string;
}

/** Represents a collection of selectable objects of type T in a table. */
export interface TableData<T> {
    /** Used to control underlying table data. */
    dataSource: MatTableDataSource<T>;
    /** Used to select/unselect rows. */
    selection?: SelectionModel<T>;
    /** Ordered to display columns (identified by key, but labeled by displayedName) from left to right. */
    keyDisplayedNameOrdering?: KeyDisplayedNameMap[];
    /** Ordered to display columns (identified by keys) from left to right. */
    keyOrdering?: string[];
}
