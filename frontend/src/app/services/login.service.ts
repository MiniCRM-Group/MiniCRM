import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, of } from 'rxjs';
import { LoginResponse } from '../models/server_responses/login-response.model';
import { retry, catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http: HttpClient) { }

  loginEndpoint = 'api/login';

  private handleError(_error: HttpErrorResponse) {
    console.log("Login Service failed!")
    return of<LoginResponse>({
      url: '/',
      loggedIn: false
    });
  }

  getLoginResponse(): Observable<LoginResponse> {
    const options = {
      responseType: 'json' as const
    };
    return this.http.get<LoginResponse>(this.loginEndpoint, options)
    .pipe(
      retry(3), // retry 3 times
      catchError(this.handleError)
    );
  }
}
