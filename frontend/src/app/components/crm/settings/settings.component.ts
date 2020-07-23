import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Settings, displayNotificationFrequencies } from 'src/app/models/server_responses/settings-response.model';
import { SettingsService } from 'src/app/services/settings.service';
import { first } from 'rxjs/operators';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {
  settings: Settings;
  displayedSettings: DisplayedSettings = {
    email: '',
    phone: '',
    language: '',
    currency: ''
  };

  constructor(private titleService: Title, private settingsService: SettingsService) {
    this.titleService.setTitle($localize`Settings`);
    this.settingsService.getSettings().pipe(first()).subscribe((res) => {
      this.settings = res.settings;
      this.createDisplayedSettings();
    });
  }

  ngOnInit(): void {
  }

  createDisplayedSettings() {
    let emailString =
    this.settings.emailNotificationsEnabled ?
      (this.settings.email.length > 0 ?
        this.settings.email :
        'Please Provide an Email') :
      'Email Notifications Disabled';
    if (this.settings.emailNotificationsEnabled && this.settings.email.length > 0) {
      emailString +=
      `(${displayNotificationFrequencies(this.settings.emailNotificationsFrequency)})`;
    }
    let phoneString =
      this.settings.phoneNotificationsEnabled ?
        (this.settings.phone.length > 0 ?
          this.settings.phone :
          'Please Provide a Phone Number') :
        'Phone Notifications Disabled';
    if (this.settings.emailNotificationsEnabled) {
      phoneString +=
      `(${displayNotificationFrequencies(this.settings.phoneNotificationsFrequency)})`;
    }
    this.displayedSettings = {
      email: emailString,
      phone: phoneString,
      language: this.settings.language,
      currency: this.settings.currency
    };
  }
}

interface DisplayedSettings {
  email: string;
  phone: string;
  language: string;
  currency: string;
}
