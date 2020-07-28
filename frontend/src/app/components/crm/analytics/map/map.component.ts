import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { } from 'googlemaps';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})

/// <reference types="googlemaps" />
export class MapComponent implements AfterViewInit {

  @ViewChild('map', {static: true}) mapElement: any;
  map: google.maps.Map;
  marker = (new google.maps.Marker);
  marker2 = new google.maps.Marker;
  infoWindow = new google.maps.InfoWindow;
  pos = null;

  constructor(private titleService: Title) {
    this.titleService.setTitle('Map');
  }

  ngAfterViewInit(): void {
    const mapProperties = {
      center: new google.maps.LatLng(39.933632, -82.887267),
      zoom: 11,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    const myLatLng = {lat: 35.2271, lng: -80.8431};

    this.map = new google.maps.Map(this.mapElement.nativeElement,    mapProperties);
    this.marker = new google.maps.Marker({
      position: myLatLng,
      map: this.map,
      title: 'Hello World!'
    });
    // Try HTML5 geolocation.
    // Try HTML5 geolocation.
    let latitude;
    let longitude;

    const geocoder = new google.maps.Geocoder();
    geocoder.geocode(
    {
       componentRestrictions: {
           country: 'ET',
           postalCode: '251'
       }
    }, function(results, status) {
        if (status === google.maps.GeocoderStatus.OK) {
            latitude = results[0].geometry.location.lat();
            longitude = results[0].geometry.location.lng();
            console.log(latitude + ',' + longitude);
        } else {
            alert('Request failed.');
        }
    });

	    navigator.geolocation.getCurrentPosition(this.success);

  }

  success(pos) {
    const crd = pos.coords;
    console.log('Your current position is:');
    console.log(`Latitude : {crd.latitude}`);
    console.log(`Longitude: {crd.longitude}`);
    console.log(`More or less {crd.accuracy} meters.`);
    const myLatLng2 = {lat: crd.latitude, lng: crd.longitude};
    console.log(myLatLng2);
  }

}
