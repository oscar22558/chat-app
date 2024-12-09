import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl} from "@angular/forms";
import {SearchUserInvitationStatusWebapiService} from "../../../service/group-invitation/search-user-webapi-service/search-user-invitation-status-webapi.service";
import {SearchUserInvitationStatusResponse} from "../../../service/group-invitation/search-user-webapi-service/search-user-invitation-status-response";
import {SendGroupInvitationService} from "../../../service/group-invitation/send-group-invitation-service/send-group-invitation.service";
import {
  GroupInvitationListWebapiService
} from "../../../service/group-invitation/group-invitation-list-webapi-service/group-invitation-list-webapi.service";
import {mergeMap} from "rxjs";
import {MatSnackBar} from "@angular/material/snack-bar";
import {
  GroupInvitationListResponse
} from "../../../service/group-invitation/group-invitation-list-webapi-service/group-invitation-list-response";
import {
  RevokeGroupInvitationWebapiService
} from "../../../service/group-invitation/revoke-group-invitation-webapi-service/revoke-group-invitation-webapi.service";

@Component({
  selector: 'app-invite-other-users-tabpage',
  templateUrl: './invite-other-users-tabpage.component.html',
  styleUrls: ['./invite-other-users-tabpage.component.sass']
})
export class InviteOtherUsersTabpageComponent {

  @Input("groupId")
  groupId: number = -1
  @Output()
  groupInvitationListUpdated = new EventEmitter<unknown>();
  @Output()
  userSearchResultUpdated = new EventEmitter<unknown>();

  searchStr = new FormControl("")
  userSearchResult: SearchUserInvitationStatusResponse = []

  constructor(
    private searchUserInvitationStatusWebapiService: SearchUserInvitationStatusWebapiService,
    private sendInvitationService: SendGroupInvitationService,
    private snackBar: MatSnackBar,
  ) {
    this.searchStr.valueChanges.subscribe(this.onSearchTextChange.bind(this))
  }

  onSearchTextChange(username: string | null){
    if(!username) {
      this.userSearchResult = []
      return
    }
    this.updateSearchResult(username)
  }

  onInviteClick(userId: number) {
    this.sendInvitationService
      .send(this.groupId, userId)
      .subscribe(() => {
        this.snackBar.open("Invitation sent.", "OK", {duration: 1000})
        this.groupInvitationListUpdated.emit()
        this.userSearchResultUpdated.emit()
      })
  }

  updateSearchResult(searchStr?: string | null){
    const username = searchStr ?? this.searchStr.value ?? ""
    this.searchUserInvitationStatusWebapiService
      .search(this.groupId, username)
      .subscribe(res => this.userSearchResult = res)
  }

  get invitationNotSentUsers(){
    return this.userSearchResult
      .filter(result => result.invitationStatus === "")
  }

}
