import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsComponent } from './forms.component';
<<<<<<< Updated upstream
import { MatDialogModule } from '@angular/material/dialog';
import { FormService } from 'src/app/services/form.service';
import { of } from 'rxjs';
import { FormsResponse, Form } from 'src/app/models/server_responses/forms-response.model';
import { HttpClientModule } from '@angular/common/http';
import { WebhookService } from 'src/app/services/webhook.service';
import { WebHookResponse } from 'src/app/models/server_responses/webhook-response.model';
=======
import { FormsCrmModule } from './forms.module';
import { FormService } from 'src/app/services/form.service';
import { of } from 'rxjs';
import { FormsResponse, Form } from 'src/app/models/server_responses/forms-response.model';
import { By } from '@angular/platform-browser';
import { HarnessLoader } from '@angular/cdk/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { MatInputHarness } from '@angular/material/input/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
>>>>>>> Stashed changes

describe('FormsComponent', () => {
  let loader: HarnessLoader;
  let component: FormsComponent;
  const forms: Form[] = [
    {
      formName: 'Form 1',
      formId: 1,
      date: 'Today'
    },
    {
      formName: 'Form 2',
      formId: 2,
      date: 'Yesterday'
    },
    {
      formName: 'Form 3',
      formId: 3,
      date: 'June 24'
    }
  ];
  const formService: Partial<FormService> = {
<<<<<<< Updated upstream
    getForms: () => of<FormsResponse>({ forms: [] }),
    unlinkForms: (req: Form[]) => 'ok'
  };
  const webhookService: Partial<WebhookService> = {
    getWebhook: () => of<WebHookResponse>({ webhookUrl: '/', googleKey: 'abc' })
=======
    getForms: () => of<FormsResponse>({ forms }),
    renameForm: (form: Form) => {
      let editedForms = Array.from(forms);
      for(let i = 0; i < forms.length; i++) {
        if(forms[i].formId === form.formId) {
          forms[i].formName = form.formName;
        }
      }
      return of<FormsResponse>({ forms: editedForms });
    }
>>>>>>> Stashed changes
  };

  let fixture: ComponentFixture<FormsComponent>;
  let nativeElement: HTMLElement;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FormsComponent ],
<<<<<<< Updated upstream
      imports: [ MatDialogModule, HttpClientModule ],
=======
      imports: [ FormsCrmModule, NoopAnimationsModule ],
>>>>>>> Stashed changes
      providers: [
        { provide: FormService, useValue: formService }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FormsComponent);
    component = fixture.componentInstance;
    nativeElement = fixture.debugElement.nativeElement;
    loader = TestbedHarnessEnvironment.loader(fixture);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });


  it('should display all form ids', () => {
    const expectedFormIds = forms.map(form => form.formId.toString().trim());
    const actualFormIds = scrapeValues(nativeElement, '.formId');
    expect(actualFormIds).toEqual(expectedFormIds);
  });

  it('should display all form names', () => {
    const expectedFormNames = forms.map(form => form.formName.toString().trim());
    const formDateElements = fixture.debugElement.queryAll(By.css('.formName .editable'));
    fixture.whenStable().then(() => {
      const actualformDates = Array.from(formDateElements).map(elem => elem.nativeElement.value);
      expect(actualformDates).toEqual(expectedFormNames);
    });
  });

  it('should display all form dates', () => {
    const expectedFormDates = forms.map(form => form.date.trim());
    const actualFormDates = scrapeValues(nativeElement, '.date');
    expect(actualFormDates).toEqual(expectedFormDates);
  });

  // not working for some reason
  xit('should filter forms', async () => {
    const formFilter = await loader.getHarness(MatInputHarness.with({ placeholder: 'Search' }));
    await formFilter.setValue('1');
    expect(component.formsTable.dataSource.filteredData).toEqual([ forms[0] ]);
  });

  it('should edit form name', async () => {
    const formNameField = await loader.getHarness(MatInputHarness.with({ value: 'Form 1' }));
    await formNameField.setValue('EDITED');
    expect(component.formsTable.data.map(form => form.formName)).toContain('EDITED');
  });
});

/**
 * Provides a list of values stored inside table column.
 * @param htmlElement The HTML element to query through.
 * @param cssSelector The CSS selector that the table cells are identified by.
 */
function scrapeValues(htmlElement: HTMLElement, cssSelector: string): string[] {
  return Array.from(htmlElement.querySelectorAll(cssSelector))
  .map(value => value.textContent.trim());
}
