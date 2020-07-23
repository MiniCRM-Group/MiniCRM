import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SettingsComponent } from './settings.component';
import { SettingsService } from 'src/app/services/settings.service';
import { of } from 'rxjs';
import { SettingsResponse } from 'src/app/models/server_responses/settings-response.model';

describe('SettingsComponent', () => {
  let component: SettingsComponent;
  const settingsService: Partial<SettingsService> = {
    getSettings: () => of<SettingsResponse>({
      settings: {
        settingsId: 1,
        email: 'test@example.com',
        emailNotificationsEnabled: true,
        emailNotificationsFrequency: {
          onEveryLead: true,
          daily: true,
          weekly: true
        },
        phone: '101-101-1010',
        phoneNotificationsEnabled: true,
        phoneNotificationsFrequency: {
          onEveryLead: true,
          daily: true,
          weekly: false
        },
        language: 'Spanish',
        currency: 'USD $'
      },
      supportedLanguages: [
        {
          language: 'Spanish',
          isoCode: 'es'
        }
      ],
      supportedCurrencies: [
        {
          currency: 'USD $',
          isoCode: 'USD'
        }
      ]
    })
  };
  let fixture: ComponentFixture<SettingsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SettingsComponent ],
      providers: [
        { provide: SettingsService, useValue: settingsService }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
