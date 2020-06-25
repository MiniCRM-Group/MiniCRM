import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

interface LoginState {

}

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http: HttpClient) { }

  loginUrl = "api/login"

  getLoginState() {
    return this.http.get(this.loginUrl);
  }
}
