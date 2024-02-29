import { TestBed } from '@angular/core/testing';

import { FriendInvitationSearchUserWebapiService } from './friend-invitation-search-user-webapi.service';

describe('FriendInvitationSearchUserWebapiService', () => {
  let service: FriendInvitationSearchUserWebapiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FriendInvitationSearchUserWebapiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
