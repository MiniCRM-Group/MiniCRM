import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { LoginService } from 'src/app/services/login.service';
import { LoginResponse } from 'src/app/models/server_responses/login-response.model';

@Component({
  selector: 'app-crm',
  templateUrl: './crm.component.html',
  styleUrls: ['./crm.component.css']
})
export class CrmComponent implements OnInit {
  isExpanded = true;
  navigationData: NavigationDatum[] = [
    {
      displayedName: 'Guide',
      link: 'guide',
      icon: 'info'
    },
    {
      displayedName: 'Forms',
      link: 'forms',
      icon: 'ballot'
    },
    {
      displayedName: 'Campaigns',
      link: 'campaigns',
      icon: 'monetization_on'
    },
    {
      displayedName: 'Leads',
      link: 'leads',
      icon: 'group'
    },
    {
      displayedName: 'Analytics',
      link: 'analytics',
      icon: 'bar_chart'
    },
    {
      displayedName: 'Settings',
      link: 'guide', // temporary link
      icon: 'settings'
    }
  ];
  logoutUrl = '/';

  constructor(public titleService: Title, private loginService: LoginService) {
    this.loginService.getLoginResponse().subscribe((res: LoginResponse) => {
      if (res.loggedIn) {
        this.logoutUrl = res.url;
      }
    });
  }

  ngOnInit(): void {
  }

  logout(): void {
    location.href = this.logoutUrl;
  }
}

interface NavigationDatum {
  displayedName: string;
  link: string;
  icon: string;
}
