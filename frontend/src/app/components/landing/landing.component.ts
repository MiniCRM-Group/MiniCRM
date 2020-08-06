import { Component, OnInit } from '@angular/core';
import { LoginService } from '../../services/login.service';
import { LoginResponse } from '../../models/server_responses/login-response.model';
import { LanguageService } from 'src/app/services/language.service';
import { environment } from './../../../environments/environment';

interface Feature {
  icon: string;
  name: string;
  description: string;
}

@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.css']
})
export class LandingComponent implements OnInit {
  features: Feature[] = [
    {
      icon: 'list_all',
      name: $localize`Sales Lead Form Compatible`,
      description: ''
    },
    {
      icon: 'table_chart',
      name: $localize`Qualify Sales Leads`,
      description: ''
    },
    {
      icon: 'email',
      name: $localize`Contact Sales Leads`,
      description: ''
    },
    {
      icon: 'bar_chart',
      name: $localize`Analyze Sales Leads`,
      description: ''
    }
  ];
  loginUrl: string;
  loginButtonLabel: string;
  supportedLanguages = this.languageService.getAllSupportedLanguages();
  languageEnabled = environment.languageEnabled;

  constructor(private loginService: LoginService, private languageService: LanguageService) { }

  ngOnInit() {
    this.loginService.getLoginResponse().subscribe((res: LoginResponse) => {
      this.loginUrl = res.loggedIn ? 'crm/guide' : res.url;
    });
  }

  login() {
    location.href = this.loginUrl;
  }

  switchLanguage(langIsoCode) {
    location.href = `/${langIsoCode}`;
  }
}
