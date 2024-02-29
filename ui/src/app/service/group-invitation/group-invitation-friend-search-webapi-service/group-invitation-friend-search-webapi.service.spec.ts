import { TestBed } from '@angular/core/testing';

import { GroupInvitationFriendSearchWebapiService } from './group-invitation-friend-search-webapi.service';

describe('GroupInvitationFriendSearchWebapiService', () => {
  let service: GroupInvitationFriendSearchWebapiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GroupInvitationFriendSearchWebapiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
