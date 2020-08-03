import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, throwError, of } from 'rxjs';
import { LoginResponse } from '../models/server_responses/login-response.model';
import { retry, catchError, first } from 'rxjs/operators';
import { LanguageService } from './language.service';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http: HttpClient, private languageService: LanguageService) { }

  loginEndpoint = '/api/login';

  private handleError(_: HttpErrorResponse) {
    console.log('Login Service failed!');
    return of<LoginResponse>({
      url: '/',
      loggedIn: false
    });
  }

  getLoginResponse(): Observable<LoginResponse> {
    const language = this.languageService.getCurrentLanguage().isoCode;
    return this.http.get<LoginResponse>(this.loginEndpoint, {
      responseType: 'json',
      params: new HttpParams().set('language', language)
    })
    .pipe(
      first(),
      retry(3), // retry 3 times
      catchError(this.handleError)
    );
  }
}
