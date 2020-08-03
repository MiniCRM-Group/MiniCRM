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
export class ChatComponent implements OnInit, AfterViewChecked {
  @ViewChild('scrollable') private scrollableHistoryContainer: ElementRef;
  conversation: Conversation = {
    name: '',
    history: [
      {
        name: 'miniBot',
        text: 'Hey there! My name is miniBot and I am a live support chatbot to help you with any miniCRM-related questions.',
        fromCurrentUser: false,
        date: 'Now'
      }
    ]
  };
  currentTextMessage = '';
  otherUserTyping: string;
  constructor() { }

  ngOnInit(): void {
    this.scrollToBottom();
  }

  ngAfterViewChecked(): void {
    this.scrollToBottom();
  }

  sendMessage(): void {
    this.conversation.history.push({
      name: 'Rod',
      text: this.currentTextMessage,
      fromCurrentUser: true,
      date: 'Now'
    });
    this.currentTextMessage = '';
    this.pretendOtherUserTyping();
  }

  pretendOtherUserTyping() {
    this.otherUserTyping = this.conversation.history[0].name;
    setTimeout(() => {
      const msg: Message = {
        name: this.conversation.history[0].name,
        date: 'Now',
        text: 'I can offer you a 5% discount!',
        fromCurrentUser: false
      };
      this.conversation.history.push(msg);
      this.otherUserTyping = undefined;
    }, 3000);
  }

  scrollToBottom(): void {
    this.scrollableHistoryContainer.nativeElement.scrollTop = this.scrollableHistoryContainer.nativeElement.scrollHeight;
  }
}
