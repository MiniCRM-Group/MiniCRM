import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { MapComponent } from './map/map.component';
import { ChartsComponent } from './charts/charts.component';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-analytics',
  templateUrl: './analytics.component.html',
  styleUrls: ['./analytics.component.css']
})
/// <reference types="googlemaps" />
export class AnalyticsComponent implements AfterViewInit {
  chartsTabLabel = $localize`Charts`;
  mapTabLabel = $localize`Map`;

  @ViewChild('map') map: MapComponent;
  @ViewChild('charts') chart: ChartsComponent;

  constructor(private titleService: Title) {
    this.titleService.setTitle($localize`Analytics`);
  }

  ngAfterViewInit(): void {
  }

}