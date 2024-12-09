import { TestBed } from '@angular/core/testing';

import { AuthedUserWebapiService } from './authed-user-webapi.service';

describe('AuthedUserWebapiService', () => {
  let service: AuthedUserWebapiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthedUserWebapiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
