import { TestBed } from '@angular/core/testing';

import { RevokeGroupInvitationWebapiService } from './revoke-group-invitation-webapi.service';

describe('RevokeGroupInvitationWebapiService', () => {
  let service: RevokeGroupInvitationWebapiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RevokeGroupInvitationWebapiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
