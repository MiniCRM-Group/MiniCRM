import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FormsComponent } from './forms.component';
import { MatDialogModule } from '@angular/material/dialog';
import { FormService } from 'src/app/services/form.service';
import { of } from 'rxjs';
import { FormsResponse, Form } from 'src/app/models/server_responses/forms-response.model';
import { LinkFormRequest } from 'src/app/models/server_requests/link-form-request.model';
import { WebHookResponse } from 'src/app/models/server_responses/webhook-response.model';

describe('FormsComponent', () => {
  let component: FormsComponent;
  let formServiceStub: Partial<FormService> = {
    getForms: () => of<FormsResponse>({ forms: [] }),
    linkForm: (req: LinkFormRequest) => of<WebHookResponse>({ webhookUrl: '', googleKey: '' }),
    unlinkForms: (req: Form[]) => 'ok'
  };
  let fixture: ComponentFixture<FormsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FormsComponent ],
      imports: [ MatDialogModule ],
      providers: [ { provide: FormService, useValue: formServiceStub } ]
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
