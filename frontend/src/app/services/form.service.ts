import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { FormsResponse } from '../models/server_responses/forms-response.model';
import { retry, catchError, first, mergeMap } from 'rxjs/operators';
import { Form } from '../models/server_responses/forms-response.model';
import { map } from 'lodash';

@Injectable({
  providedIn: 'root'
})
export class FormService {

  formEndpoint = '/api/forms';
  private formMap: Map<number, string>;

  constructor(private http: HttpClient) { 
    this.generateFormMap();
  }

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
    //update the form in the map TODO: move this to the success callback... what does mergeMap do
    this.formMap.set(form.formId, form.formName);
    return this.http.put<any>(this.formEndpoint, body)
    .pipe(retry(3), mergeMap(() => this.getForms()));
  }

  /**
   * Utilizes the formMap to get the name of a form given its id in constant time
   * @param formId the form id of the form name to get. 
   * @return the name of the form specified by the form id. Undefined if the form does not exist.
   */
  getFormName(formId: number): string {
    return this.formMap.get(formId);
  }

  /**
   * Initially generates the formMap that maps formId to formName for constant lookup on service creation
   */
  private generateFormMap() {
    let forms: Form[];
    this.getForms().subscribe((res: FormsResponse) => {
      forms = res.forms;
      console.log(forms);
      let map: Map<number, string> = new Map();
      for (let form of forms) {
        map.set(form.formId, form.formName);
      }
      this.formMap = map;
    });
    
  }
  
}
