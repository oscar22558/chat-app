import { TestBed } from '@angular/core/testing';

import { ContactsStompService } from './contacts-stomp.service';

describe('ContactsStompService', () => {
  let service: ContactsStompService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ContactsStompService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
