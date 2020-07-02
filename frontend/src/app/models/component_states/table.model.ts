/** Represents the column's name s displayed on a table. */
type DisplayedColumnname = string;
/** Represents the keys in an object as the columns in a table. */
type ColumnName = string;
/** Represents the allowed types in a column. */
type ColumnType = any;

export type TableRowInput = Map<ColumnName, ColumnType>;
export type TableInput = TableRowInput[];

/** Maps an object key to a table-displayed column name. */
export interface NameMap {
    columnName: ColumnName;
    displayedColumnName: DisplayedColumnname;
}
/** Represents a singular object in a table row. */
export interface TableRow {
    /** Datum received from a service to display in a table row. */
    datum: Map<ColumnName, ColumnType>;
    /** Represents whether a particular row is selected. */
    isSelected: boolean;
}
/** Represents a collection of objects in a table. */
export interface Table {
    /** Ordered list of table rows. */
    tableRows: TableRow[];
    /** Ordered list of object keys mapped to displayed column names. */
    nameMaps: NameMap[];
    /** Selects all rows. */
    selectAll: () => any;
    /** Returns any selected rows. */
    getSelected: () => TableRow[];
    /** Filters by a particular column name. */
    filter: (key: ColumnName, value: ColumnType) => TableRow[];
    /** Sorts table rows by a particular column name. */
    sort: (key: ColumnName, ascending: boolean) => any;
}
