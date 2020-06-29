import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginResponse } from '../types/responses/login-response';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http: HttpClient) { }

  loginEndpoint = 'api/login';

  getLoginResponse(): Observable<LoginResponse> {
    const options = {
      responseType: 'json' as const
    };
    return this.http.get<LoginResponse>(this.loginEndpoint, options);
  }
}
