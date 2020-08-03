import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { SettingsComponent } from './settings.component';
import { SettingsService } from 'src/app/services/settings.service';
import { of } from 'rxjs';
import { SettingsResponse, Settings } from 'src/app/models/server_responses/settings-response.model';
import { MatListHarness } from '@angular/material/list/testing';
import { HarnessLoader } from '@angular/cdk/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { LoginService } from 'src/app/services/login.service';
import { LoginResponse } from 'src/app/models/server_responses/login-response.model';

describe('SettingsComponent', () => {
  const settings: Settings = {
    email: 'test@example.com',
    emailNotificationsFrequency: 'Never',
    phone: '101-101-1010',
    phoneNotificationsFrequency: 'On Every Lead',
    language: 'Spanish',
    currency: 'USD $'
  };
  let loader: HarnessLoader;
  let component: SettingsComponent;
  const settingsService: Partial<SettingsService> = {
    getSettings: () => of<SettingsResponse>({
      settings,
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
        },
        {
          displayed: 'On Every Lead',
          id: 'ON_EVERY_LEAD'
        }
      ]
    })
  };
  const loginService: Partial<LoginService> = {
    getLoginResponse: () => of<LoginResponse>({
      loggedIn: false,
      url: ''
    })
  };
  let fixture: ComponentFixture<SettingsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SettingsComponent ],
      providers: [
        { provide: SettingsService, useValue: settingsService },
        { provide: LoginService, useValue: loginService }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SettingsComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });


  it('should display settings', async () => {
    const settingsList = await loader.getHarness(MatListHarness);
    const [notifications, langAndCurrency] = await settingsList.getItemsGroupedByDividers();

    const [emailNotifications, phoneNotifications] = notifications;

    const [emailLabel, email] = await emailNotifications.getLinesText();
    expect(emailLabel).toEqual('Email');
    expect(email).toEqual(settings.emailNotificationsFrequency);

    const [phoneLabel, phone] = await phoneNotifications.getLinesText();
    expect(phoneLabel).toEqual('Phone Number');
    expect(phone).toEqual(`${settings.phone} (${settings.phoneNotificationsFrequency})`);

    const [language, currency] = langAndCurrency;

    const [langLabel, lang] = await language.getLinesText();
    expect(langLabel).toEqual('Language');
    expect(lang).toEqual(settings.language);

    const [currLabel, curr] = await currency.getLinesText();
    expect(currLabel).toEqual('Currency');
    expect(curr).toEqual(settings.currency);
  });
});
