import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Lead, LeadsResponse } from '../models/server_responses/lead.model';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class LeadService {
    private url = '/api/leads';

    constructor(private http: HttpClient) { }

    getAllLeads(): Observable<Lead[]> {
      const options = {
        responseType: 'json' as const
      };
      let leadsResponse : Observable<LeadsResponse> = this.http.get<LeadsResponse>(this.url, options);
      console.log(leadsResponse);
      let leads : Observable<Lead[]> = leadsResponse.pipe(
        map(res => res.leads)
      );
      console.log(leads);
      return leads;
    }
}
