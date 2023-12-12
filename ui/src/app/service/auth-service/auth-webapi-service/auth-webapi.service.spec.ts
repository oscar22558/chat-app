import { TestBed } from '@angular/core/testing';

import { AuthWebapiService } from './auth-webapi.service';

describe('AuthWebapiService', () => {
  let service: AuthWebapiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthWebapiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
