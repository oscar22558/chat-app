import { TestBed } from '@angular/core/testing';

import { GroupMemberWebapiService } from './group-member-webapi.service';

describe('GroupMemberWebapiService', () => {
  let service: GroupMemberWebapiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GroupMemberWebapiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
