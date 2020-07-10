import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Lead } from '../models/server_responses/lead.model';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class LeadService {
    private url = '/api/leads';

    constructor(
      private http: HttpClient) { }
      getAllLeads(): Observable<Lead[]> {
      return this.http.get<Lead[]>(this.url);
    }
}
