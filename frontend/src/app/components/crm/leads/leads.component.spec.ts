import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LeadsComponent } from './leads.component';
import { MatDialogModule } from '@angular/material/dialog';
import { LeadService } from 'src/app/services/lead.service';
import { Observable, of } from 'rxjs';
import { LeadsResponse } from 'src/app/models/server_responses/lead.model';

describe('LeadsComponent', () => {
  let component: LeadsComponent;
  let leadService: LeadService;
  let fixture: ComponentFixture<LeadsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ MatDialogModule ],
      declarations: [ LeadsComponent ],
      providers: [
        {
          provide: LeadService,
          useValue: {
            getAllLeads: (): Observable<LeadsResponse> => of({
              leads: []
            })
          }
        }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LeadsComponent);
    component = fixture.componentInstance;
    leadService = TestBed.get(LeadService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
