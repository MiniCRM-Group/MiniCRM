import { Injectable } from '@angular/core';
import {HttpClient } from '@angular/common/http';
import { Lead } from '../leads/model/lead.model';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';

@Injectable()
export class LeadService {
  constructor(private http: HttpClient) {}
   private url = './api/webhook';
  getAllLeads(): Observable<Lead[]> {
       return this.http.get<Lead[]>(this.url);
    }

}
