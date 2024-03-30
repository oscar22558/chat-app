import { TestBed } from '@angular/core/testing';

import { SubscribeMessageStompService } from './subscribe-message-stomp.service';

describe('SubscribeMessageStompService', () => {
  let service: SubscribeMessageStompService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SubscribeMessageStompService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
