import { TestBed } from '@angular/core/testing';

import { GroupWebapiService } from './group-webapi.service';

describe('GroupWebapiService', () => {
  let service: GroupWebapiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GroupWebapiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
