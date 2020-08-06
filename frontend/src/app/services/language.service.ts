import { Injectable } from '@angular/core';
import { Language } from '../models/server_responses/settings-response.model';

@Injectable({
  providedIn: 'root'
})
export class LanguageService {
  private readonly DEFAULT: Language = {
    isoCode: '',
    displayed: 'English'
  };
  private readonly SUPPORTED: Language[] = [
    {
      isoCode: 'es',
      displayed: 'Spanish'
    },
    {
      isoCode: 'hi',
      displayed: 'Hindi'
    },
    {
      isoCode: 'pt',
      displayed: 'Portuguese'
    }
  ];
  constructor() { }
  getCurrentLanguage(): Language {
    const path = location.pathname;
    const language = this.SUPPORTED.find(lang => path.startsWith('/' + lang.isoCode));
    return language ?? this.DEFAULT;
  }

  getAllSupportedLanguages(): Language[] {
    return [ this.DEFAULT, ...this.SUPPORTED ];
  }
}
