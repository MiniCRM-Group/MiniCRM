import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FormsResponse } from '../models/server_responses/forms-response.model';
import { LinkFormResponse } from '../models/server_responses/link-form-response.model';
import { WebHookResponse } from '../models/server_responses/webhook-response.model';

@Injectable({
  providedIn: 'root'
})
export class FormService {

  constructor(private http: HttpClient) { }

  formEndpoint = 'api/forms';

  getForms(): Observable<FormsResponse> {
    const options = {
      responseType: 'json' as const
    };
    return this.http.get<FormsResponse>(this.formEndpoint, options);
  }

  linkForm(linkFormResponse: LinkFormResponse): Observable<WebHookResponse> {
    const options = {
      responseType: 'json' as const
    };
    return this.http.post<WebHookResponse>(this.formEndpoint, linkFormResponse, options)
  }
}
