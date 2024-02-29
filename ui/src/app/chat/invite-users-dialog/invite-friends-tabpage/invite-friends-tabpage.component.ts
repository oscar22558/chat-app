import {Component, EventEmitter, Inject, Input, OnInit, Output} from '@angular/core';
import {GroupInvitationFriendSearchWebapiService} from "../../../service/group-invitation/group-invitation-friend-search-webapi-service/group-invitation-friend-search-webapi.service";
import {FormControl} from "@angular/forms";
import {GroupInvitationFriendSearchResponse} from "../../../service/group-invitation/group-invitation-friend-search-webapi-service/group-invitation-friend-search-response";
import {
  SendGroupInvitationService
} from "../../../service/group-invitation/send-group-invitation-service/send-group-invitation.service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-invite-friends-tabpage',
  templateUrl: './invite-friends-tabpage.component.html',
  styleUrls: ['./invite-friends-tabpage.component.sass']
})
export class InviteFriendsTabpageComponent implements OnInit{
  @Input()
  groupId = -1
  @Output()
  groupInvitationListUpdated = new EventEmitter<unknown>()
  @Output()
  userSearchResultUpdated = new EventEmitter<unknown>();

  searchStr = new FormControl("")
  friendList: GroupInvitationFriendSearchResponse = []
  constructor(
    private friendListWebapiService: GroupInvitationFriendSearchWebapiService,
    private sendGroupInvitationWebapiService: SendGroupInvitationService,
    private matSnackBar: MatSnackBar,
  ) {
    this.searchStr.valueChanges.subscribe(this.updateSearchResult.bind(this))
  }

  ngOnInit(): void {
    this.updateSearchResult()
  }

  updateSearchResult(newSearchStr?: string | null){
    this.friendListWebapiService
      .get(this.groupId, newSearchStr ?? this.searchStr.value ?? "")
      .subscribe(res => this.friendList = res)
  }

  onInviteClick(userId: number) {
    this.sendGroupInvitationWebapiService
      .send(this.groupId, userId)
      .subscribe(() => {
        this.matSnackBar.open("Invitation Sent.", "OK", {duration: 1000})
        this.groupInvitationListUpdated.emit()
        this.userSearchResultUpdated.emit()
      })
  }
}
