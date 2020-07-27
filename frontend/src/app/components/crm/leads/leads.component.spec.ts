import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { LeadsModule } from './leads.module';
import { LeadService } from 'src/app/services/lead.service';
import { LeadsComponent } from './leads.component';
import { of } from 'rxjs';
import { Lead } from 'src/app/models/server_responses/lead.model';
import { NoopAnimationsModule} from '@angular/platform-browser/animations';

describe('LeadsComponent', () => {
  let component: LeadsComponent;
  const leadService: Partial<LeadService> = {
    getAllLeads: () => of<Lead[]>([])
  };
  let fixture: ComponentFixture<LeadsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ LeadsModule, NoopAnimationsModule],
     // aotSummaries: LeadsModuleNgSummary,
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
