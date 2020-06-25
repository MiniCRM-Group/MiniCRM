import { Component } from '@angular/core';

interface Feature {
  icon: string,
  name: string,
  description: string
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  features: Feature[] = [
    {
      icon: "list_all",
      name: "Lead Forms Compatible",
      description: ""
    },
    {
      icon: "table_chart",
      name: "Qualify Leads",
      description: ""
    },
    {
      icon: "email",
      name: "Contact Leads",
      description: ""
    },
    {
      icon: "bar_chart",
      name: "Analyze Leads",
      description: ""
    }
  ];
}
