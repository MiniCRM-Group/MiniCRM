import { Component, OnInit } from '@angular/core';
import { LeadService } from '../shared/lead.service';

@Component({
  selector: 'app-leads',
  templateUrl: './leads.component.html',
  styleUrls: ['./leads.component.css']
})
export class LeadsComponent implements OnInit {

   constructor() { }

    ngOnInit(): void {
    }

}
