import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';


@Component({
  selector: 'app-guide',
  templateUrl: './guide.component.html',
  styleUrls: ['./guide.component.css']
})
export class GuideComponent implements OnInit {

  constructor(private titleService: Title) {
    this.titleService.setTitle($localize `Guide`);
  }

  ngOnInit(): void {
  }

}
