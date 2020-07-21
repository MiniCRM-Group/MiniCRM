import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Lead, LeadsResponse } from '../models/server_responses/lead.model';
import { Observable } from 'rxjs';
import { map, first } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class LeadService {
    public url = '/api/leads';

    constructor(private http: HttpClient) { }

    getAllLeads(): Observable<Lead[]> {
      const options = {
        responseType: 'json' as const
      };
      return this.http.get<LeadsResponse>(this.url, options).pipe(
        first(),
        map(res => res.leads)
      );
    }
}
