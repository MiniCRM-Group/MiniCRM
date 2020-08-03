import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Settings, Language, Currency, NotificationsFrequency, SettingsResponse } from 'src/app/models/server_responses/settings-response.model';
import { SettingsService } from 'src/app/services/settings.service';
import { first } from 'rxjs/operators';
import { FormControl, FormGroupDirective, NgForm, Validators, AbstractControl, FormGroup } from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material/core';
import { matFormFieldAnimations } from '@angular/material/form-field';
import { LoginService } from 'src/app/services/login.service';
import { LoginResponse } from 'src/app/models/server_responses/login-response.model';

/** Error when invalid control is dirty, touched, or submitted. */
export class SettingsErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}

export const phoneValidator = (control: AbstractControl): {[key: string]: any} | null => {
  const validPhoneRegex: RegExp = /(?:(?:\+?1\s*(?:[.-]\s*)?)?(?:(\s*([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9]‌​)\s*)|([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9]))\s*(?:[.-]\s*)?)([2-9]1[02-9]‌​|[2-9][02-9]1|[2-9][02-9]{2})\s*(?:[.-]\s*)?([0-9]{4})\s*(?:\s*(?:#|x\.?|ext\.?|extension)\s*(\d+)\s*)?$/i;
  return validPhoneRegex.test(control.value) ? null : { phone: control.valid };
};

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {
  settings: Settings;
  availableCurrencies: Currency[];
  availableNotifFreqs: NotificationsFrequency[];

  editMode = false;
  settingsForm = new FormGroup({
    email: new FormControl(),
    phone: new FormControl(),
    emailNotificationsFrequency: new FormControl(),
    phoneNotificationsFrequency: new FormControl(),
    currency: new FormControl()
  });
  matcher = new SettingsErrorStateMatcher();

  constructor(private titleService: Title, private settingsService: SettingsService, private loginServce: LoginService) {
    this.titleService.setTitle($localize`Settings`);
    this.settingsService.getSettings().subscribe((res) => {
      this.settings = res.settings;
      this.availableCurrencies = res.supportedCurrencies;
      this.availableNotifFreqs = res.supportedNotifsFreqs;
      this.fillForm();
      this.setValidators();
    });
  }

  ngOnInit(): void {
  }

  onEdit() {
    this.editMode = true;
  }

  onCancel() {
    this.editMode = false;
    this.fillForm();
  }

  onSave() {
    this.editMode = false;
    console.log(this.settingsForm.value);
    this.settingsService.setSettings(this.settingsForm.value).subscribe((settingsRes: SettingsResponse) => {
      this.settings = settingsRes.settings;
    }, (error) => {
      // TODO: handle error with Toaster
    }, () => {
      this.fillForm();
    });
  }

  private fillForm() {
    this.settingsForm.setValue({
      email: this.settings.email,
      emailNotificationsFrequency: this.availableNotifFreqs.find(nf => nf.displayed === this.settings.emailNotificationsFrequency).id,
      phone: this.settings.phone,
      phoneNotificationsFrequency: this.availableNotifFreqs.find(nf => nf.displayed === this.settings.phoneNotificationsFrequency).id,
      currency: this.availableCurrencies.find(curr => curr.displayed === this.settings.currency).isoCode
    });
    this.settingsForm.get('email').setValidators(
      this.settings.emailNotificationsFrequency === 'Never' ?
      null : [Validators.required, Validators.email]
    );
    this.settingsForm.get('phone').setValidators(
      this.settings.phoneNotificationsFrequency === 'Never' ?
      null : [Validators.required, phoneValidator]
    );
  }

  private setValidators() {
    this.settingsForm.get('emailNotificationsFrequency').valueChanges.subscribe((res) => {
      if (res !== 'NEVER') {
        this.settingsForm.get('email').setValidators([Validators.required, Validators.email]);
      } else {
        this.settingsForm.get('email').setValidators(null);
        this.settingsForm.patchValue({ emailFormControl: '' });
      }
      this.settingsForm.updateValueAndValidity();
    });
    this.settingsForm.get('phoneNotificationsFrequency').valueChanges.subscribe((res) => {
      console.log(res);
      if (res !== 'NEVER') {
        this.settingsForm.get('phone').setValidators([Validators.required, phoneValidator]);
      } else {
        this.settingsForm.get('phone').clearValidators();
        this.settingsForm.patchValue({ phone: '' });
      }
      this.settingsForm.get('phone').updateValueAndValidity();
    });
  }
}
