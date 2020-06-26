import{ ColumnData } from './column-data.model';

export interface Lead {
   date:Date;
   leadId:string;
   campaignId:number;
   gclId:string;
   apiVersion:string;
   formId:number;
   googleKey:string;
   userColumnData: ColumnData[];
   columnData : Map<String, String>;
   isTest : boolean;
   adgroupId : number;
   creativeId : number;

 }
