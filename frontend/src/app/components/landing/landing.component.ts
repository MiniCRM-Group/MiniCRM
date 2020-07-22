import { Component, OnInit } from '@angular/core';
import { LoginService } from '../../services/login.service';
import { LoginResponse } from '../../models/server_responses/login-response.model';

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
      name: $localize`Lead Forms Compatible`,
      description: ''
    },
    {
      icon: 'table_chart',
      name: $localize`Qualify Leads`,
      description: ''
    },
    {
      icon: 'email',
      name: $localize`Contact Leads`,
      description: ''
    },
    {
      icon: 'bar_chart',
      name: $localize`Analyze Leads`,
      description: ''
    }
  ];
  loginUrl: string;

  constructor(private loginService: LoginService) { }

  ngOnInit() {
    this.loginService.getLoginResponse().subscribe((res: LoginResponse) => {
      this.loginUrl = res.loggedIn ? 'crm/guide' : res.url;
    });
  }

  login() {
    location.href = this.loginUrl;
  }
}
