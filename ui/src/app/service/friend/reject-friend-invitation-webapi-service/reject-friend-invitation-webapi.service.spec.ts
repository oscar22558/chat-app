import { TestBed } from '@angular/core/testing';

import { RejectFriendInvitationWebapiService } from './reject-friend-invitation-webapi.service';

describe('RejectFriendInvitationWebapiService', () => {
  let service: RejectFriendInvitationWebapiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RejectFriendInvitationWebapiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
