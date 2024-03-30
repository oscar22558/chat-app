import { TestBed } from '@angular/core/testing';

import { GetConversionWebapiService } from './get-conversion-webapi.service';

describe('GetConversionWebapiService', () => {
  let service: GetConversionWebapiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GetConversionWebapiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
