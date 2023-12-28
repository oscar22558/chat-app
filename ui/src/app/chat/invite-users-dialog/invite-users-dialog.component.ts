import {Component, Inject, Input} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {InviteUsersDialogData} from "./model/invite-users-dialog-data";

@Component({
  selector: 'app-member-add-dialog',
  templateUrl: './invite-users-dialog.component.html',
  styleUrls: ['./invite-users-dialog.component.sass']
})
export class InviteUsersDialogComponent {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: InviteUsersDialogData,
  ) { }

  getGroupId(){
    return this.data.groupId
  }

}
