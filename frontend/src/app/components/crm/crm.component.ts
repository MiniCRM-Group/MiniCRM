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
  webhookUrlLabel = $localize `Your Webhook URL`;
  googleKeyLabel = $localize `Your Google Key`;
  navigationData: NavigationDatum[] = [
    {
      displayedName: $localize `Guide`,
      link: 'guide',
      icon: 'info'
    },
    {
      displayedName: $localize `Forms`,
      link: 'forms',
      icon: 'ballot'
    },
    {
      displayedName: $localize `Campaigns`,
      link: 'campaigns',
      icon: 'monetization_on'
    },
    {
      displayedName: $localize `Leads`,
      link: 'leads',
      icon: 'group'
    },
    {
      displayedName: $localize `Analytics`,
      link: 'analytics',
      icon: 'bar_chart'
    },
    {
      displayedName: $localize `Settings`,
      link: 'settings',
      icon: 'settings'
    }
  ];
  logoutUrl = '/';
  webhookUrl = '';
  googleKey = '';

  constructor(public titleService: Title, private loginService: LoginService, private webhookService: WebhookService) {
    this.loginService.getLoginResponse().subscribe((res: LoginResponse) => {
      if (res.loggedIn) {
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
