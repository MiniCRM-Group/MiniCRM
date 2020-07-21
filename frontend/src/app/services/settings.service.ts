import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Settings, LanguageType, CurrencyType } from '../models/server_responses/settings-response.model';

@Injectable({
  providedIn: 'root'
})
export class SettingsService {
  settingsEndpoint = '/api/settings';

  constructor(private http: HttpClient) { }

  getSettings(): Observable<Settings> {
    return of<Settings>({
      email: 'test@example.com',
      phone: '901-901-9090',
      receiveEmails: true,
      receiveTexts: true,
      language: LanguageType.ENGLISH,
      currency: CurrencyType.USD
    });
  }
}
