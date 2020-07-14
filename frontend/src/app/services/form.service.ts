import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { FormsResponse } from '../models/server_responses/forms-response.model';
import { LinkFormRequest } from '../models/server_requests/link-form-request.model';
import { WebHookResponse } from '../models/server_responses/webhook-response.model';
import { retry, catchError } from 'rxjs/operators';
import { Form } from '../models/server_responses/forms-response.model';

@Injectable({
  providedIn: 'root'
})
export class FormService {

  constructor(private http: HttpClient) { }

  formEndpoint = '/api/forms';

  getForms(): Observable<FormsResponse> {
    const options = {
      responseType: 'json' as const
    };
    return this.http.get<FormsResponse>(this.formEndpoint, options)
    .pipe(
      retry(3),
      catchError((_error: HttpErrorResponse) => {
        // return no forms and empty webhook url
        return of<FormsResponse>({ forms: [] });
      })
    )
  }

  linkForm(linkFormRequest: LinkFormRequest): Observable<WebHookResponse> {
    const options = {
      responseType: 'json' as const
    };
    return this.http.post<WebHookResponse>(this.formEndpoint, linkFormRequest, options)
    .pipe(
      retry(3),
      catchError((error: HttpErrorResponse) => {
        throw error.message;
      })
    );
  }

  unlinkForms(formsToUnlink: Form[]): any {
    const formIds = formsToUnlink.map((form: Form) => form.formId);
    let httpParams: HttpParams = new HttpParams();
    formIds.forEach((formId: number) => {
      httpParams = httpParams.append('formIds[]', formId.toString());
    });
    const options = {
      responseType: 'json' as const,
      params: httpParams
    };
    return this.http.delete<any>(this.formEndpoint, options);
  }
}
