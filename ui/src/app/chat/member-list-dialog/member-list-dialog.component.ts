import {Component, Inject, OnInit} from '@angular/core';
import {
  GroupMemberWebapiService
} from "../../service/group-member/group-member-webapi-service/group-member-webapi.service";
import {MAT_DIALOG_DATA, MatDialog} from "@angular/material/dialog";
import {MemberListDialogData} from "./member-list-dialog-data";
import {
  GroupMemberListResponse
} from "../../service/group-member/group-member-webapi-service/group-member-list-response";
import {DeleteMemberDialogComponent} from "../delete-member-dialog/delete-member-dialog.component";

@Component({
  selector: 'app-member-list-dialog',
  templateUrl: './member-list-dialog.component.html',
  styleUrls: ['./member-list-dialog.component.sass']
})
export class MemberListDialogComponent implements OnInit{
  memberList: GroupMemberListResponse = []
  constructor(
    private groupMemberWebapiService: GroupMemberWebapiService,
    private matDialog: MatDialog,
    @Inject(MAT_DIALOG_DATA) public data: MemberListDialogData
  ) { }

  ngOnInit(): void {
    this.groupMemberWebapiService
      .getMemberList(this.data.groupId)
      .subscribe(memberList => this.memberList = memberList)
  }

  onDeleteClick(userId: number, username: string) {
    const data = {
      groupId: this.data.groupId,
      userId,
      username
    }
    this.matDialog.open(DeleteMemberDialogComponent, {data})
  }

  getGroupName(){
    return this.data.groupName
  }
}
