import { TestBed } from '@angular/core/testing';

import { LeaveGroupWebapiService } from './leave-group-webapi.service';

describe('LeaveGroupWebapiService', () => {
  let service: LeaveGroupWebapiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LeaveGroupWebapiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
