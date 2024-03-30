import {Component, EventEmitter, OnDestroy, OnInit, Output} from '@angular/core';
import {ContactsWebapiService} from "./contacts-webapi-service/contacts-webapi.service";
import {ContactsStompService} from "./contacts-stomp-service/contacts-stomp.service";
import {ContactResponse} from "./model/contact-response";
import {MatDialog} from "@angular/material/dialog";
import {InviteUsersDialogComponent} from "../invite-users-dialog/invite-users-dialog.component";
import {GroupDeleteDialogComponent} from "../group-delete-dialog/group-delete-dialog.component";
import {MemberListDialogComponent} from "../member-list-dialog/member-list-dialog.component";
import {RecipientType} from "./model/recipient-type";
import {ChatStartedEventModel} from "../model/chat-started-event-model";

@Component({
  selector: 'app-contacts',
  templateUrl: './contacts.component.html',
  styleUrls: ['./contacts.component.sass'],
  providers: [
    ContactsWebapiService,
    ContactsStompService,
  ]
})
export class ContactsComponent implements OnInit, OnDestroy {
  contacts: ContactResponse = []
  @Output()
  onChatStarted = new EventEmitter<ChatStartedEventModel>()
  constructor(
    private contactsWebapiService: ContactsWebapiService,
    private contactsStompService: ContactsStompService,
    private matDialog: MatDialog,
  ) {}

  ngOnInit(): void {
    this.contactsWebapiService
      .getContacts()
      .subscribe(res => this.contacts = res)
    this.contactsStompService
      .subscribeContactUpdate(newContacts => this.contacts = newContacts)
  }

  ngOnDestroy() {
    this.contactsStompService.unsubscribe()
  }

  onInviteUsersClick(groupId: number, groupName: string){
    const data = {groupId, groupName}
    this.matDialog.open(InviteUsersDialogComponent, {data})
  }
  onGroupDeleteClick(groupId: number, groupName: string) {
    const data = {groupId, groupName}
    this.matDialog.open(GroupDeleteDialogComponent, {data})
  }

  onMemberListClick(groupId: number, groupName: string) {
    const data = {groupId, groupName}
    this.matDialog.open(MemberListDialogComponent, {data})
  }

  onChatClick(recipientId: number, recipientType: RecipientType) {
    this.onChatStarted.emit({recipientId, recipientType})
  }
}
