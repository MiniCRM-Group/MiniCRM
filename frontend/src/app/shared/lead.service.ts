import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders } from '@angular/common/http';
import { Lead } from '../leads/model/lead.model';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';


@Injectable()
export class LeadService {
private url = 'http://localhost:8080/api/webhook';
 httpOptions = {


    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };
    constructor(
      private http: HttpClient) { }


      getAllLeads(): Observable<Lead[]> {
      return this.http.get<Lead[]>(this.url);

    }

 }
