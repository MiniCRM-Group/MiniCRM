import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { SettingsResponse, Settings } from '../models/server_responses/settings-response.model';
import { retry, first } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class SettingsService {
  settingsEndpoint = '/api/settings';

  constructor(private http: HttpClient) { }

  getSettings(): Observable<SettingsResponse> {
    return this.http.get<SettingsResponse>(this.settingsEndpoint).pipe(first(), retry(3));
  }

  setSettings(newSettings: Settings): Observable<SettingsResponse> {
    return this.http.put<SettingsResponse>(this.settingsEndpoint, newSettings).pipe(first(), retry(3));
  }
}
