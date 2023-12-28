import { Component } from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {GroupCreateDialogComponent} from "./group-create-dialog/group-create-dialog.component";

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.sass']
})
export class ChatComponent {
  constructor(private dialog: MatDialog) { }
  onCreateGroupClick() {
    const ref = this.dialog.open(GroupCreateDialogComponent);
    ref.afterClosed().subscribe(result => {

    })
  }
}
