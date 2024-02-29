import { TestBed } from '@angular/core/testing';

import { AcceptFriendInvitationWebapiService } from './accept-friend-invitation-webapi.service';

describe('AcceptFriendInvitationWebapiService', () => {
  let service: AcceptFriendInvitationWebapiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AcceptFriendInvitationWebapiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
