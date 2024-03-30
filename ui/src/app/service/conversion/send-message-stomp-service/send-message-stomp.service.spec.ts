import { TestBed } from '@angular/core/testing';

import { SendMessageStompService } from './send-message-stomp.service';

describe('SendMessageStompService', () => {
  let service: SendMessageStompService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SendMessageStompService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
