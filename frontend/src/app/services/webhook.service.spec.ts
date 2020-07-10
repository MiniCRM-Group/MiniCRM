import { TestBed } from '@angular/core/testing';

import { WebhookService } from './webhook.service';

describe('WebhookServiceService', () => {
  let service: WebhookService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WebhookService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
