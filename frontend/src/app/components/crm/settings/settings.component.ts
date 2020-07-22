import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Settings } from 'src/app/models/server_responses/settings-response.model';
import { SettingsService } from 'src/app/services/settings.service';
import { first } from 'rxjs/operators';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {
  settings: Settings;

  constructor(private titleService: Title, private settingsService: SettingsService) {
    this.titleService.setTitle($localize`Settings`);
    this.settingsService.getSettings().pipe(first()).subscribe((settings) => {
      this.settings = settings;
    });
  }

  ngOnInit(): void {
  }

}
