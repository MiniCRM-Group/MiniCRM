import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { LoginService } from 'src/app/services/login.service';
import { LoginResponse } from 'src/app/models/server_responses/login-response.model';
import { WebhookService } from 'src/app/services/webhook.service';
import { WebHookResponse } from 'src/app/models/server_responses/webhook-response.model';
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
      link: 'settings',
      icon: 'settings'
    }
  ];
  logoutUrl = '/';
  webhookUrl = '';
  googleKey = '';

  constructor(public titleService: Title, private loginService: LoginService, private webhookService: WebhookService) {
    this.loginService.getLoginResponse().subscribe((res: LoginResponse) => {
      if (!res.loggedIn) {
        location.href = this.logoutUrl;
      } else {
        this.logoutUrl = res.url;
      }
    });
    this.webhookService.getWebhook().subscribe((res: WebHookResponse) => {
      this.webhookUrl = res.webhookUrl;
      this.googleKey = res.googleKey;
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
