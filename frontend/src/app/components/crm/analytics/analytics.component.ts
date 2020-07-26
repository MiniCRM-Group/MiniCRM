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
  infoWindow = new google.maps.InfoWindow;
  constructor(private titleService: Title) {
    this.titleService.setTitle('Analytics');
  }
 
  ngAfterViewInit(): void {
    const mapProperties = {
      center: new google.maps.LatLng(35.2271, -80.8431),
      zoom: 15,
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
if (navigator.geolocation) {
  navigator.geolocation.getCurrentPosition(function(position) {
    var pos = {
      lat: position.coords.latitude,
      lng: position.coords.longitude
    };

    this.infoWindow.setPosition(pos);
    this.infoWindow.setContent('Location found.');
    this.infoWindow.open(this.map);
    this.map.setCenter(pos);
  }, function() {
    handleLocationError(true, this.infoWindow, this.map.getCenter());
  });
} else {
  // Browser doesn't support Geolocation
  handleLocationError(false, this.infoWindow, this.map.getCenter());
}


function handleLocationError(browserHasGeolocation, infoWindow, pos) {
infoWindow.setPosition(pos);
infoWindow.setContent(browserHasGeolocation ?
                      'Error: The Geolocation service failed.' :
                      'Error: Your browser doesn\'t support geolocation.');
infoWindow.open(this.map);
}
  }

}
