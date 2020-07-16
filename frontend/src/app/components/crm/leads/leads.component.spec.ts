import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LeadsComponent } from './leads.component';
import { MatDialogModule } from '@angular/material/dialog';
import { LeadService } from 'src/app/services/lead.service';
import { of } from 'rxjs';
import { Lead } from 'src/app/models/server_responses/lead.model';

describe('LeadsComponent', () => {
  let component: LeadsComponent;
  const leadService: Partial<LeadService> = {
    getAllLeads: () => of<Lead[]>([])
  };
  let fixture: ComponentFixture<LeadsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ MatDialogModule ],
      declarations: [ LeadsComponent ],
      providers: [
        { provide: LeadService, useValue: leadService }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LeadsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
