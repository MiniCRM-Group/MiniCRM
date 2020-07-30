import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { FormsResponse } from '../models/server_responses/forms-response.model';
import { retry, catchError, first, mergeMap } from 'rxjs/operators';
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
      first(),
      retry(3),
      catchError((_: HttpErrorResponse) => {
        // return no forms and empty webhook url
        return of<FormsResponse>({ forms: [] });
      })
    );
  }

  renameForm(form: Form): Observable<FormsResponse> {
    const body = {formId: form.formId.toString(), formName: form.formName};
    return this.http.put<any>(this.formEndpoint, body)
    .pipe(retry(3), mergeMap(() => this.getForms()));
  }
}
