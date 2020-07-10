import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { WebHookResponse } from '../models/server_responses/webhook-response.model';

@Injectable({
  providedIn: 'root'
})
export class WebhookService {

  constructor(private http: HttpClient) { }

  webhookEndpoint = '/api/webhook';

  getWebhook(): Observable<WebHookResponse> {
    return this.http.get<WebHookResponse>(this.webhookEndpoint);
  }
}
