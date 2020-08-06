import { Component, ViewChild, AfterViewInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { } from 'googlemaps';

import { LeadService } from '../../../../services/lead.service';
import { Lead } from '../../../../models/server_responses/lead.model';

import { first, map } from 'rxjs/operators';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})

/// <reference types="googlemaps" />
export class MapComponent implements AfterViewInit {

  @ViewChild('map', {static: true}) mapElement: any;
  hidden = false;
  map: google.maps.Map;
  allMarkers: google.maps.Marker[] = [];
  marker = new google.maps.Marker();
  marker2 = new google.maps.Marker();
  infoWindow = new google.maps.InfoWindow();
  heatmap = new google.maps.visualization.HeatmapLayer();
  pos = null;
  leads: Lead[];
  constructor(private leadService: LeadService, private titleService: Title) {
    this.titleService.setTitle('Map');
    this.loadAllHeatLocations();
    this.loadAllMarkerLocations();
  }

  ngAfterViewInit(): void {
    // Set default map properties
    const mapProperties = {
      center: new google.maps.LatLng(37.782551, -122.445368),
      zoom: 11,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };

    this.map = new google.maps.Map(this.mapElement.nativeElement, mapProperties);

    // Subcribing to leads here to avoid the common array bug
    this.leadService.getAllLeads().subscribe((leads: Lead[]) => {
      const markerLocations = this.getAllMarkersLocation(leads);
      markerLocations.forEach((markerLocation: number[]) => {
        const marker = new google.maps.Marker({
          position: new google.maps.LatLng(markerLocation[0], markerLocation[1]),
          map: this.map
        });
        this.allMarkers.push(marker);
      });
    });

    // Heatmap loading starts here
    this.heatmap = new google.maps.visualization.HeatmapLayer({
      data: this.loadAllHeatLocations(),
      map: this.map
    });

    // Get user's location if possible
    const userLocation = new Promise<any>((resolve, reject) => {
      navigator.geolocation.getCurrentPosition(resp => {
          resolve({lat: resp.coords.latitude, lng: resp.coords.longitude });
        },
        err => {
          reject(err);
        });
    });
    userLocation.then((pos) => {
      this.map.setCenter(new google.maps.LatLng(pos.lat, pos.lng) );
    });

  }

  handleButtonClick() {
    if (this.hidden) {
      this.allMarkers.forEach((marker: google.maps.Marker) => {
        marker.setMap(this.map);
      });
    } else {
      this.allMarkers.forEach((marker: google.maps.Marker) => {
        marker.setMap(null);
      });
    }

    if (this.hidden) {
      this.hidden = false;
    } else {
      this.hidden = true;
    }
  }

  loadAllMarkerLocations() {
    const points = [];
    return this.leadService.getAllLeads().pipe(first(), map((leads) => {
      this.leads = leads;
      const latitudeCollection = leads.map(lead => lead.estimatedLatitude);
      const longitudeCollection = leads.map(lead => lead.estimatedLongitude);
      for (let i = 0; i < latitudeCollection.length; i++) {
        if (latitudeCollection[i] !== undefined) {
          const x = latitudeCollection[i];
          const y = longitudeCollection[i];
          points.push([x, y]);
        }
      }
      return points;
    }));
  }

  getAllMarkersLocation(leads: Lead[]): number[][] {
    const points = [];
    const latitudeCollection = leads.map(lead => lead.estimatedLatitude);
    const longitudeCollection = leads.map(lead => lead.estimatedLongitude);
    for (let i = 0; i < latitudeCollection.length; i++) {
      if (latitudeCollection[i] !== undefined) {
        const x = latitudeCollection[i];
        const y = longitudeCollection[i];
        points.push([x, y]);
      }
    }
    return points;
  }

  loadAllHeatLocations() {
    const points = [];
    this.leadService.getAllLeads().pipe(first()).subscribe((leads) => {
      this.leads = leads;
      const latitudeCollection = leads.map(lead => lead.estimatedLatitude);
      const longitudeCollection = leads.map(lead => lead.estimatedLongitude);
      for (let i = 0; i < latitudeCollection.length; i++) {
        if (latitudeCollection[i] !== undefined) {
          const x = latitudeCollection[i];
          const y = longitudeCollection[i];
          points.push(new google.maps.LatLng(x, y));
      }
     }
    });
    return points;
  }

}
