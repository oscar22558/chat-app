import { TestBed } from '@angular/core/testing';

import { FriendListWebapiService } from './friend-list-webapi.service';

describe('FriendListWebapiService', () => {
  let service: FriendListWebapiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FriendListWebapiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
