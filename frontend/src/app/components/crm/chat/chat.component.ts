import { Component, OnInit, ViewChild, ElementRef, AfterViewChecked } from '@angular/core';

export interface Message {
  name: string;
  date: string;
  text: string;
  fromCurrentUser: boolean;
}

export interface Conversation {
  name: string;
  history: Message[];
}

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {
  constructor() {

  }
  ngOnInit(): void {}
}
