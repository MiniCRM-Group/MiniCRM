import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { FormsResponse } from '../models/server_responses/forms-response.model';
import { LinkFormResponse } from '../models/server_responses/link-form-response.model';
import { WebHookResponse } from '../models/server_responses/webhook-response.model';
import { retry, catchError } from 'rxjs/operators';

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
    return this.http.get<FormsResponse>(this.formEndpoint, options)
    .pipe(
      retry(3),
      catchError((_error: HttpErrorResponse) => {
        // return no forms and empty webhook url
        return of<FormsResponse>({
          webhookUrl: '',
          forms: []
        });
      })
    )
  }

  linkForm(linkFormResponse: LinkFormResponse): Observable<WebHookResponse> {
    const options = {
      responseType: 'json' as const
    };
    return this.http.post<WebHookResponse>(this.formEndpoint, linkFormResponse, options)
    .pipe(
      retry(3),
      catchError((error: HttpErrorResponse) => {
        throw error.message;
      })
    );
  }
}
