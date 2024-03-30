import { TestBed } from '@angular/core/testing';

import { StompConnectionService } from './stomp-connection.service';

describe('StompConnectionService', () => {
  let service: StompConnectionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StompConnectionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
