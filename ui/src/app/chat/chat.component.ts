import { Component } from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {GroupCreateDialogComponent} from "./group-create-dialog/group-create-dialog.component";
import {ChatStartedEventModel} from "./model/chat-started-event-model";
import {RecipientType} from "./contacts/model/recipient-type";

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.sass']
})
export class ChatComponent {
  recipientId: number = -1
  recipientType: RecipientType = "USER"
  constructor(private dialog: MatDialog) { }
  onCreateGroupClick() {
    const ref = this.dialog.open(GroupCreateDialogComponent);
    ref.afterClosed().subscribe(result => {

    })
  }

  onChatStarted({recipientId, recipientType}: ChatStartedEventModel) {
    this.recipientId = recipientId
    this.recipientType = recipientType
  }
}
