import {Component, EventEmitter, Output} from '@angular/core';
import {FormControl} from "@angular/forms";
import {
  FriendInvitationSearchUserWebapiService
} from "../service/friend/friend-invitation-search-user-webapi-service/friend-invitation-search-user-webapi.service";
import {
  FriendInvitationSearchUserResponse
} from "../service/friend/friend-invitation-search-user-webapi-service/friend-invitation-search-user-response";
import {
  SendFriendInvitationWebapiService
} from "../service/friend/send-friend-invitation-webapi-service/send-friend-invitation-webapi.service";
import {catchError, throwError} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";
import {MatSnackBar} from "@angular/material/snack-bar";
import GenericHttpErrorHandler from "../service/generic-http-error-handler";

@Component({
  selector: 'app-send-friend-request',
  templateUrl: './send-friend-invitation.component.html',
  styleUrls: ['./send-friend-invitation.component.sass']
})
export class SendFriendInvitationComponent {
  usernameSearchStr = new FormControl("")
  searchResult: FriendInvitationSearchUserResponse = []
  @Output()
  friendInvitationsListUpdatedEvent = new EventEmitter<unknown>()
  constructor(
    private friendInvitationSearchUserWebapiService: FriendInvitationSearchUserWebapiService,
    private sendFriendInvitationWebapiService: SendFriendInvitationWebapiService,
    private snackBar: MatSnackBar,
    )
  {
    this.usernameSearchStr.valueChanges.subscribe(this.onSearchValChange.bind(this))
  }

  onSearchValChange(val: string|null){
    if(!val){
      this.searchResult = []
      return
    }
    this.friendInvitationSearchUserWebapiService
      .searchUsers(val ?? "")
      .subscribe(res => this.searchResult = res)
  }
  fetchSearchResult(){
    this.onSearchValChange(this.usernameSearchStr.value)
  }

  onInviteClick(userId: number){
    this.sendFriendInvitationWebapiService
      .sendRequest(userId)
      .pipe(
        catchError(new GenericHttpErrorHandler().build())
      )
      .subscribe(()=> {
        this.friendInvitationsListUpdatedEvent.emit()
        this.fetchSearchResult()
        this.snackBar.open("Invitation sent.", "OK", {duration: 1000})
      })
  }
}
