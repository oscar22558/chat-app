import { TestBed } from '@angular/core/testing';

import { GroupInvitationListWebapiService } from './group-invitation-list-webapi.service';

describe('GroupInvitationListWebapiService', () => {
  let service: GroupInvitationListWebapiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GroupInvitationListWebapiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
