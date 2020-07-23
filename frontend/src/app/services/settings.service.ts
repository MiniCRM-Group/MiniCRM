import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { SettingsResponse } from '../models/server_responses/settings-response.model';

@Injectable({
  providedIn: 'root'
})
export class SettingsService {
  settingsEndpoint = '/api/settings';

  constructor(private http: HttpClient) { }

  getSettings(): Observable<SettingsResponse> {
    return this.http.get<SettingsResponse>(this.settingsEndpoint);
  }
}
