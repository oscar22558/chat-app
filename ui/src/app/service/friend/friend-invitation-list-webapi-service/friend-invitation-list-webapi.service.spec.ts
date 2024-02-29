import { TestBed } from '@angular/core/testing';

import { FriendInvitationListWebapiService } from './friend-invitation-list-webapi.service';

describe('FriendInvitationListWebapiService', () => {
  let service: FriendInvitationListWebapiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FriendInvitationListWebapiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
