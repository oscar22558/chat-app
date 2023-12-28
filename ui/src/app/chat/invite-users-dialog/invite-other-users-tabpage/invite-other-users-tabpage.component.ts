import {Component, Input, OnInit} from '@angular/core';
import {FormControl} from "@angular/forms";
import {SearchUserInvitationStatusWebapiService} from "../../../service/search-user-webapi-service/search-user-invitation-status-webapi.service";
import {SearchUserInvitationStatusResponse} from "../../../service/search-user-webapi-service/search-user-invitation-status-response";
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
export class InviteOtherUsersTabpageComponent implements OnInit {

  @Input("groupId")
  groupId: number = -1
  friendUsername = new FormControl("")
  userSearchResult: SearchUserInvitationStatusResponse = []
  groupInvitations: GroupInvitationListResponse = []
  constructor(
    private searchUserInvitationStatusWebapiService: SearchUserInvitationStatusWebapiService,
    private sendInvitationService: SendGroupInvitationService,
    private groupInvitationListService: GroupInvitationListWebapiService,
    private revokeGroupInvitationWebapiService: RevokeGroupInvitationWebapiService,
    private snackBar: MatSnackBar,
  ) {
    this.friendUsername.valueChanges.subscribe(this.onSearchTextChange.bind(this))
  }

  onSearchTextChange(username: string | null){
    if(!username) {
      this.userSearchResult = []
      return
    }
    this.searchUserInvitationStatusWebapiService
      .search(this.groupId, username ?? "")
      .subscribe(res => this.userSearchResult = res)
  }

  onInviteClick(userId: number) {
    this.sendInvitationService
      .send(this.groupId, userId)
      .pipe(
        mergeMap(() => {
          this.snackBar.open("Invitation sent.", "OK", {duration: 1000})
          return this.groupInvitationListService.get(this.groupId)
        }),
        mergeMap(invitationList => {
          this.groupInvitations = invitationList
          return this.searchUserInvitationStatusWebapiService
            .search(this.groupId, this.friendUsername.value ?? "")
        })
      )
      .subscribe(searchResult => this.userSearchResult = searchResult)
  }

  onRevokeClick(userId: number){
    this.revokeGroupInvitationWebapiService
      .revoke(this.groupId, userId)
      .pipe(
        mergeMap(() => {
          this.snackBar.open("Invitation revoked." , "OK", {duration: 1000})
          return this.groupInvitationListService.get(this.groupId)
        }),
        mergeMap(invitationList =>{
          this.groupInvitations = invitationList
          return this.searchUserInvitationStatusWebapiService
            .search(this.groupId, this.friendUsername.value ?? "")
        })
      )
      .subscribe(searchResult => this.userSearchResult = searchResult)
  }

  get invitationNotSentUsers(){
    console.log("invitationNotAcceptedUsers called")
    return this.userSearchResult
      .filter(result => result.invitationStatus == "")
  }

  ngOnInit(): void {
    this.groupInvitationListService
      .get(this.groupId)
      .subscribe(invitationList =>
        this.groupInvitations = invitationList
      )
  }
}
