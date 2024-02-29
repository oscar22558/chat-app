import { TestBed } from '@angular/core/testing';

import { RevokeFriendInvitationWebapiService } from './revoke-friend-invitation-webapi.service';

describe('RevokeFriendInvitationWebapiService', () => {
  let service: RevokeFriendInvitationWebapiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RevokeFriendInvitationWebapiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
