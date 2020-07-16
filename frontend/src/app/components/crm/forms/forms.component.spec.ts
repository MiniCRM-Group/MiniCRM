import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FormsComponent } from './forms.component';
import { MatDialogModule } from '@angular/material/dialog';
import { FormService } from 'src/app/services/form.service';
import { of } from 'rxjs';
import { FormsResponse, Form } from 'src/app/models/server_responses/forms-response.model';
import { HttpClientModule } from '@angular/common/http';
import { WebhookService } from 'src/app/services/webhook.service';
import { WebHookResponse } from 'src/app/models/server_responses/webhook-response.model';

describe('FormsComponent', () => {
  let component: FormsComponent;
  const formService: Partial<FormService> = {
    getForms: () => of<FormsResponse>({ forms: [] }),
    unlinkForms: (req: Form[]) => 'ok'
  };
  const webhookService: Partial<WebhookService> = {
    getWebhook: () => of<WebHookResponse>({ webhookUrl: '/', googleKey: 'abc' })
  };
  let fixture: ComponentFixture<FormsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FormsComponent ],
      imports: [ MatDialogModule, HttpClientModule ],
      providers: [
        { provide: FormService, useValue: formService },
        { provide: WebhookService, useValue: webhookService }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FormsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
