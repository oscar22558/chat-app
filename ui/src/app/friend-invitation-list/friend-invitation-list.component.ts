import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {
  FriendInvitationListWebapiService
} from "../service/friend/friend-invitation-list-webapi-service/friend-invitation-list-webapi.service";
import {catchError} from "rxjs";
import GenericHttpErrorHandler from "../service/generic-http-error-handler";
import {
  FriendInvitationListResponse
} from "../service/friend/friend-invitation-list-webapi-service/friend-invitation-list-response";
import {
  RevokeFriendInvitationWebapiService
} from "../service/friend/revoke-friend-invitation-webapi-service/revoke-friend-invitation-webapi.service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-friend-invitation-list',
  templateUrl: './friend-invitation-list.component.html',
  styleUrls: ['./friend-invitation-list.component.sass']
})
export class FriendInvitationListComponent implements OnInit{
  friendInvitations: FriendInvitationListResponse = []
  @Output()
  friendInvitationUserSearchResultUpdatedEvent = new EventEmitter<any>()
  constructor(private friendInvitationListWebapiService: FriendInvitationListWebapiService,
              private revokeFriendInvitationWebapiService: RevokeFriendInvitationWebapiService,
              private snackBar: MatSnackBar,
              ) { }

  ngOnInit() {
    this.fetchFriendInvitationList()
  }

  fetchFriendInvitationList(){
    this.friendInvitationListWebapiService
      .getInvitations("SENT")
      .pipe(
        catchError(new GenericHttpErrorHandler().build())
      )
      .subscribe(res => this.friendInvitations = res)
  }

  onRevokeClick(userId: number) {
    this.revokeFriendInvitationWebapiService
      .revoke(userId)
      .pipe(
        catchError(new GenericHttpErrorHandler().build())
      )
      .subscribe(() => {
        this.friendInvitationUserSearchResultUpdatedEvent.emit()
        this.fetchFriendInvitationList()
        this.snackBar.open("Invitation Revoked.", "OK", {duration: 1000})
      })
  }

}
