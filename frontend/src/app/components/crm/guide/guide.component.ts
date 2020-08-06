import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { STEPPER_GLOBAL_OPTIONS } from '@angular/cdk/stepper';

@Component({
  selector: 'app-guide',
  templateUrl: './guide.component.html',
  styleUrls: ['./guide.component.css'],
  providers: [{
    provide: STEPPER_GLOBAL_OPTIONS, useValue: {displayDefaultIndicatorType: false}
  }]
})
export class GuideComponent implements OnInit {

  constructor(private titleService: Title) {
    this.titleService.setTitle($localize`Guide`);
  }

  ngOnInit(): void {
  }

}
