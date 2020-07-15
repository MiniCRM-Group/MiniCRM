import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { MatTooltip } from '@angular/material/tooltip';

@Component({
  selector: 'app-copyable-form-field',
  templateUrl: './copyable-form-field.component.html',
  styleUrls: ['./copyable-form-field.component.css']
})
export class CopyableFormFieldComponent implements OnInit {
  @Input() label: string;
  @Input() copyableValue: string;
  tooltipMessage: string = 'Copied';
  @ViewChild('tooltip') tooltip: MatTooltip; 

  constructor() { }

  ngOnInit(): void {
  }

  showTooltip(): void {
    this.tooltip.show();
    // setTimeout(() => {
    //   this.tooltip.hide();
    // }, 500);
  }

}
