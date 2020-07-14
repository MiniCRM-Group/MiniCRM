import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-campaigns',
  templateUrl: './campaigns.component.html',
  styleUrls: ['./campaigns.component.css']
})
export class CampaignsComponent implements OnInit {

  constructor(private titleService: Title) {
    this.titleService.setTitle('Campaigns');
  }

  ngOnInit(): void {
  }

}
