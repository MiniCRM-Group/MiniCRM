export interface ColumnData {
  FULL_NAME: string;
  PHONE_NUMBER: string;
  EMAIL: string;
}

// An interface to handle fetching a lead's property
export interface Lead {
   date: Date;
   leadId: string;
   campaignId: number;
   gclId: string;
   apiVersion: string;
   formId: number;
   googleKey: string;
   columnData: ColumnData;
   isTest: boolean;
   adgroupId: number;
   creativeId: number;
   status: string;
   notes: string;
}

export interface LeadsResponse {
  leads: Lead[];
}
