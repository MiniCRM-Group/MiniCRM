import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { CrmComponent } from './crm.component';
import { LoginService } from 'src/app/services/login.service';
import { of } from 'rxjs';
import { WebhookService } from 'src/app/services/webhook.service';
import { WebHookResponse } from 'src/app/models/server_responses/webhook-response.model';
import { LoginResponse } from 'src/app/models/server_responses/login-response.model';
import { MatMenuModule } from '@angular/material/menu';

import { CrmModule } from './crm.module';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('CrmComponent', () => {
  let component: CrmComponent;
  const loginService: Partial<LoginService> = {
    getLoginResponse: () => of<LoginResponse>({ loggedIn: false, url: '/' })
  };
  const webhookService: Partial<WebhookService> = {
    getWebhook: () => of<WebHookResponse>({ webhookUrl: '/', googleKey: 'abc' })
  };
  let fixture: ComponentFixture<CrmComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CrmComponent ],
      imports: [
        CrmModule, NoopAnimationsModule
       ],
      providers: [
        { provide: LoginService, useValue: loginService },
        { provide: WebhookService, useValue: webhookService }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CrmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
