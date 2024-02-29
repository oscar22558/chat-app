import {Component, Inject, Input, ViewChild} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {InviteUsersDialogData} from "./model/invite-users-dialog-data";
import {GroupInvitationListComponent} from "./group-invitation-list/group-invitation-list.component";
import {InviteFriendsTabpageComponent} from "./invite-friends-tabpage/invite-friends-tabpage.component";
import {InviteOtherUsersTabpageComponent} from "./invite-other-users-tabpage/invite-other-users-tabpage.component";

@Component({
  selector: 'app-member-add-dialog',
  templateUrl: './invite-users-dialog.component.html',
  styleUrls: ['./invite-users-dialog.component.sass']
})
export class InviteUsersDialogComponent {
  @ViewChild(GroupInvitationListComponent)
  groupInvitationListComponent?: GroupInvitationListComponent

  @ViewChild(InviteFriendsTabpageComponent)
  inviteFriendsTabpageComponent?: InviteFriendsTabpageComponent

  @ViewChild(InviteOtherUsersTabpageComponent)
  inviteOtherUsersTabpageComponent?: InviteOtherUsersTabpageComponent

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: InviteUsersDialogData,
  ) { }

  getGroupId(){
    return this.data.groupId
  }

  onGroupInvitationListUpdated() {
    this.groupInvitationListComponent?.fetchGroupInvitationList()
  }

  onSearchResultUpdated(){
    this.inviteOtherUsersTabpageComponent?.updateSearchResult()
    this.inviteFriendsTabpageComponent?.updateSearchResult()
  }
}
