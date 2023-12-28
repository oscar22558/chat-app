import { TestBed } from '@angular/core/testing';

import { SendGroupInvitationService } from './send-group-invitation.service';

describe('SendGroupInvitationService', () => {
  let service: SendGroupInvitationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SendGroupInvitationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
