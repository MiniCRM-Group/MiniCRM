import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { LeadDetailsComponent } from './lead-details.component';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Lead } from 'src/app/models/server_responses/lead.model';
import { HumanizeEnumPipe } from 'src/app/pipes/humanize-enum.pipe';


describe('LeadDetailsComponent', () => {
  let component: LeadDetailsComponent;
  let fixture: ComponentFixture<LeadDetailsComponent>;
  const matDialogRef: Partial<MatDialogRef<LeadDetailsComponent>> = {
    close: jasmine.createSpy('close')
  };
  const data: any = {
    lead: {
      date: new Date(2020, /*month=*/ 7, /*day=*/ 17),
      leadId: '1',
      campaignId: 1,
      gclId: '1',
      apiVersion: '1.0.0',
      formId: 1,
      googleKey: 'abc',
      columnData: {
        FULL_NAME: 'Roddy Taipe',
        PHONE_NUMBER: '973-201-2019',
        EMAIL: 'roddy@roddy.com',
        POSTAL_CODE: '43004',
        STREET_ADDRESS: '3975 Townsfair Way',
        CITY: 'Columbus',
        REGION: 'OH',
        COUNTRY: 'US'
      },
      isTest: true,
      adgroupId: 1,
      creativeId: 1,
      status: 'OPEN',
      notes: 'This is a good lead.'
    } as Lead
  };

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LeadDetailsComponent, HumanizeEnumPipe ],
      imports: [
        MatDialogModule
      ],
      providers: [
        { provide: MatDialogRef, useValue: matDialogRef },
        { provide: MAT_DIALOG_DATA, useValue: data }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LeadDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
