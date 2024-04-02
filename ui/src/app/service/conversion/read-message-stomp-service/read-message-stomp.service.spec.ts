import { TestBed } from '@angular/core/testing';

import { ReadMessageStompService } from './read-message-stomp.service';

describe('ReadMessageStompService', () => {
  let service: ReadMessageStompService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReadMessageStompService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
