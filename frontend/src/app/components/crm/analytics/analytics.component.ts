import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-analytics',
  templateUrl: './analytics.component.html',
  styleUrls: ['./analytics.component.css']
})
export class AnalyticsComponent implements OnInit {

  constructor(private titleService: Title) {
    this.titleService.setTitle($localize `Analytics`);
  }

  ngOnInit(): void {
  }

}
