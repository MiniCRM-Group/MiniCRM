import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import {} from 'googlemaps';

@Component({
  selector: 'app-analytics',
  templateUrl: './analytics.component.html',
  styleUrls: ['./analytics.component.css']
})
export class AnalyticsComponent implements OnInit {

  constructor(private titleService: Title) {
    this.titleService.setTitle('Analytics');
  }

  ngOnInit(): void {
    let map: google.maps.Map;

    function initMap(): void {
      map = new google.maps.Map(document.getElementById("map") as HTMLElement, {
        center: { lat: -34.397, lng: 150.644 },
        zoom: 8
      });
    }
  }

}
