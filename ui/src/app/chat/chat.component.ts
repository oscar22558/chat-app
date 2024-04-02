import {Component} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {GroupCreateDialogComponent} from "./group-create-dialog/group-create-dialog.component";
import {Recipient} from "./model/recipient";
import {RecipientType} from "./contacts/model/recipient-type";
import {Optional} from "../common/optional";

@Component({
    selector: 'app-chat',
    templateUrl: './chat.component.html',
    styleUrls: ['./chat.component.sass']
})
export class ChatComponent {
    recipient: Optional<Recipient>

    constructor(private dialog: MatDialog) {
    }

    onCreateGroupClick() {
        const ref = this.dialog.open(GroupCreateDialogComponent);
        ref.afterClosed().subscribe()
    }

    onChatStarted(recipient: Recipient) {
        this.recipient = recipient
    }
}
