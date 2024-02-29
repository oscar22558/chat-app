import {Component, ViewChild} from '@angular/core';
import {SendFriendInvitationComponent} from "../send-friend-invitation/send-friend-invitation.component";
import {FriendInvitationListComponent} from "../friend-invitation-list/friend-invitation-list.component";
import {FriendListComponent} from "../friend-list/friend-list.component";

@Component({
  selector: 'app-friend',
  templateUrl: './friend.component.html',
  styleUrls: ['./friend.component.sass']
})
export class FriendComponent {
  @ViewChild(FriendInvitationListComponent)
  private friendInvitationListComponent?: FriendInvitationListComponent
  @ViewChild(SendFriendInvitationComponent)
  private sendFriendRequestComponent?: SendFriendInvitationComponent
  @ViewChild(FriendListComponent)
  private friendListComponent?: FriendListComponent
  onFriendInvitationListUpdated(){
    this.friendInvitationListComponent?.fetchFriendInvitationList()
  }

  onFriendInvitationUserSearchResultUpdated() {
    this.sendFriendRequestComponent?.fetchSearchResult()
  }

  onFriendListUpdated() {
    this.friendListComponent?.fetchFriendList()
  }
}
