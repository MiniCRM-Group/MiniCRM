import { ColumnData } from './column-data.model';

// An interface to handle fetching a lead's property
export interface Lead {
   date: Date;
   lead_id: string;
   campaign_id: number;
   gcl_id: string;
   api_version: string;
   form_id: number;
   google_Key: string;
   column_data: ColumnData;
   is_test: boolean;
   adgroup_id: number;
   creative_id: number;

 }
