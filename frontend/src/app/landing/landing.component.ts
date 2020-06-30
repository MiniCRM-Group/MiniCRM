import { Component } from '@angular/core';
import { LoginService } from '../services/login.service';
import { LoginResponse } from '../models/login-response.model';

interface Feature {
  icon: string,
  name: string,
  description: string
}

@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.css']
})
export class LandingComponent {
  features: Feature[] = [
    {
      icon: "list_all",
      name: "Lead Forms Compatible",
      description: ""
    },
    {
      icon: 'table_chart',
      name: 'Qualify Leads',
      description: ''
    },
    {
      icon: 'email',
      name: 'Contact Leads',
      description: ''
    },
    {
      icon: 'bar_chart',
      name: 'Analyze Leads',
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

  linkToLoginPage() {
    location.href = this.loginUrl;
  }
}
