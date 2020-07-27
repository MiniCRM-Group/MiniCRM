import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import {} from 'googlemaps';

@Component({
  selector: 'app-analytics',
  templateUrl: './analytics.component.html',
  styleUrls: ['./analytics.component.css']
})
/// <reference types="googlemaps" />
export class AnalyticsComponent implements AfterViewInit {

  
  @ViewChild('map', {static: true}) mapElement: any;
  map: google.maps.Map;
  marker = new google.maps.Marker;
    marker2 = new google.maps.Marker;
  infoWindow = new google.maps.InfoWindow;
 pos = null;
  constructor(private titleService: Title) {
    this.titleService.setTitle('Analytics');
  }
 
  ngAfterViewInit(): void {
    const mapProperties = {
      center: new google.maps.LatLng(39.933632, -82.887267),
      zoom: 11,
      mapTypeId: google.maps.MapTypeId.ROADMAP
 };
 var myLatLng = {lat: 35.2271, lng: -80.8431};
 
 this.map = new google.maps.Map(this.mapElement.nativeElement,    mapProperties);
 this.marker = new google.maps.Marker({
  position: myLatLng,
  map: this.map,
  title: 'Hello World!'
});
// Try HTML5 geolocation.
// Try HTML5 geolocation.


	navigator.geolocation.getCurrentPosition(this.success);

  }

 success(pos) {
  var crd = pos.coords;

  console.log('Your current position is:');
  console.log(`Latitude : {crd.latitude}`);
  console.log(`Longitude: {crd.longitude}`);
  console.log(`More or less {crd.accuracy} meters.`);
 var myLatLng2 = {lat: crd.latitude, lng: crd.longitude};

}



}
