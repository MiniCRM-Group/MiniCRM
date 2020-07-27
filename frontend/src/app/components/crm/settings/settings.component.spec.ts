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
        email: 'test@example.com',
        emailNotificationsFrequency: 'Never',
        phone: '101-101-1010',
        phoneNotificationsFrequency: 'Never',
        language: 'Spanish',
        currency: 'USD $'
      },
      supportedLanguages: [
        {
          displayed: 'Spanish',
          isoCode: 'es'
        }
      ],
      supportedCurrencies: [
        {
          displayed: 'USD $',
          isoCode: 'USD'
        }
      ],
      supportedNotifsFreqs: [
        {
          displayed: 'Never',
          id: 'NEVER'
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
