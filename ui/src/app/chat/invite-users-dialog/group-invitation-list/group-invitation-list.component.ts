import {Component, EventEmitter, Inject, Input, OnInit, Output} from '@angular/core';
import {
  GroupInvitationListWebapiService
} from "../../../service/group-invitation/group-invitation-list-webapi-service/group-invitation-list-webapi.service";
import {
  GroupInvitationListResponse
} from "../../../service/group-invitation/group-invitation-list-webapi-service/group-invitation-list-response";
import {mergeMap} from "rxjs";
import {
  RevokeGroupInvitationWebapiService
} from "../../../service/group-invitation/revoke-group-invitation-webapi-service/revoke-group-invitation-webapi.service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-group-invitation-list',
  templateUrl: './group-invitation-list.component.html',
  styleUrls: ['./group-invitation-list.component.sass']
})
export class GroupInvitationListComponent implements OnInit{
  @Input()
  groupId = -1
  @Output()
  userSearchResultUpdated = new EventEmitter<unknown>()

  groupInvitations: GroupInvitationListResponse = []
  constructor(
    private groupInvitationListWebapiService: GroupInvitationListWebapiService,
    private revokeGroupInvitationWebapiService: RevokeGroupInvitationWebapiService,
    private snackBar: MatSnackBar,
  ) { }

  ngOnInit(): void {
    this.fetchGroupInvitationList()
  }

  fetchGroupInvitationList(){
    this.groupInvitationListWebapiService
      .get(this.groupId)
      .subscribe(res => this.groupInvitations = res)
  }

  onRevokeClick(userId: number) {
    this.revokeGroupInvitationWebapiService
      .revoke(this.groupId, userId)
      .pipe(
        mergeMap(() => {
          this.userSearchResultUpdated.emit()
          this.snackBar.open("Revoked invitation.", "OK", {duration: 1000})
          return this.groupInvitationListWebapiService.get(this.groupId)
        }),
      )
      .subscribe(invitations => {
        this.groupInvitations = invitations
      })
  }
}
