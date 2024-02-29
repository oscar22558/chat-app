import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {
  FriendInvitationListWebapiService
} from "../service/friend/friend-invitation-list-webapi-service/friend-invitation-list-webapi.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {catchError} from "rxjs";
import GenericHttpErrorHandler from "../service/generic-http-error-handler";
import {
  FriendInvitationListResponse
} from "../service/friend/friend-invitation-list-webapi-service/friend-invitation-list-response";
import {
  RejectFriendInvitationWebapiService
} from "../service/friend/reject-friend-invitation-webapi-service/reject-friend-invitation-webapi.service";
import {
  AcceptFriendInvitationWebapiService
} from "../service/friend/accept-friend-invitation-webapi-service/accept-friend-invitation-webapi.service";

@Component({
  selector: 'app-friend-request-list',
  templateUrl: './friend-request-list.component.html',
  styleUrls: ['./friend-request-list.component.sass']
})
export class FriendRequestListComponent implements OnInit{
  friendRequests: FriendInvitationListResponse = []
  @Output()
  friendInvitationUserSearchResultUpdatedEvent = new EventEmitter()
  @Output()
  friendListUpdatedEvent = new EventEmitter()
  constructor(private friendInvitationListWebapiService: FriendInvitationListWebapiService,
              private rejectFriendInvitationWebapiService: RejectFriendInvitationWebapiService,
              private acceptFriendInvitationWebapiService: AcceptFriendInvitationWebapiService,
              private snackBar: MatSnackBar,
              ) { }

  ngOnInit() {
    this.fetchFriendRequestList()
  }

  fetchFriendRequestList(){
    this.friendInvitationListWebapiService
      .getInvitations("RECEIVED")
      .pipe(
        catchError(new GenericHttpErrorHandler().build())
      )
      .subscribe(res => this.friendRequests = res)
  }

  onRejectClick(userId: number) {
    this.rejectFriendInvitationWebapiService
      .reject(userId)
      .pipe(
        catchError(new GenericHttpErrorHandler().build())
      )
      .subscribe(() => {
        this.friendInvitationUserSearchResultUpdatedEvent.emit()
        this.fetchFriendRequestList()
        this.snackBar.open("Rejected.", "OK", {duration: 1000})
      })
  }


  onAcceptClick(userId: number) {
    this.acceptFriendInvitationWebapiService
      .accept(userId)
      .pipe(
        catchError(new GenericHttpErrorHandler().build())
      )
      .subscribe(() => {
        this.friendListUpdatedEvent.emit()
        this.fetchFriendRequestList()
        this.snackBar.open("Invitation Accepted.", "OK", {duration: 1000})
      })
  }
}
