import { TestBed } from '@angular/core/testing';

import { SendFriendInvitationWebapiService } from './send-friend-invitation-webapi.service';

describe('SendFriendInvitationWebapiService', () => {
  let service: SendFriendInvitationWebapiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SendFriendInvitationWebapiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
