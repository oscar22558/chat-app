import { TestBed } from '@angular/core/testing';

import { SearchUserInvitationStatusWebapiService } from './search-user-invitation-status-webapi.service';

describe('SearchUserWebapiService', () => {
  let service: SearchUserInvitationStatusWebapiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SearchUserInvitationStatusWebapiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
