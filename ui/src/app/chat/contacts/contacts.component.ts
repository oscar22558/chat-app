import {Component, OnInit} from '@angular/core';
import {ContactsWebapiService} from "./contacts-webapi-service/contacts-webapi.service";
import {ContactsStompService} from "./contacts-stomp-service/contacts-stomp.service";
import {ContactResponse} from "./model/contact-response";

@Component({
  selector: 'app-contacts',
  templateUrl: './contacts.component.html',
  styleUrls: ['./contacts.component.sass'],
  providers: [
    ContactsWebapiService,
    ContactsStompService,
  ]
})
export class ContactsComponent implements OnInit {
  contacts: ContactResponse = []
  constructor(
    private contactsWebapiService: ContactsWebapiService,
    private contactsStompService: ContactsStompService,
  ) {}

  ngOnInit(): void {
    this.contactsWebapiService
      .getContacts()
      .subscribe(res => this.contacts = res)
    this.contactsStompService.initStomp()
    this.contactsStompService
      .subscribeContactUpdate(newContacts => this.contacts = newContacts)
  }
}
