import { TestBed } from '@angular/core/testing';

import { ContactsWebapiService } from './contacts-webapi.service';

describe('ContactsWebapiService', () => {
  let service: ContactsWebapiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ContactsWebapiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
